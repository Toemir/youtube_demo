package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.AttachDTO;
import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentInfoDTO;
import com.company.dto.comment.CommentUpdateDTO;
import com.company.dto.comment.CommentCreateDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.CommentEntity;
import com.company.enums.ProfileRole;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.MethodNotAllowedException;
import com.company.repository.CommentInfoRepository;
import com.company.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    private final ProfileService profileService;

    private final VideoService videoService;

    private final AttachService attachService;

    private final CommentLikeService commentLikeService;


    public CommentService(CommentRepository commentRepository, ProfileService profileService, VideoService videoService, AttachService attachService, CommentLikeService commentLikeService) {
        this.commentRepository = commentRepository;
        this.profileService = profileService;
        this.videoService = videoService;
        this.attachService = attachService;
        this.commentLikeService = commentLikeService;
    }

    public void create(CommentCreateDTO dto) {
        CommentEntity entity = toEntity(dto);

        if (dto.getCommentId() != null
                && !commentRepository.existsByIdAndVisible(
                dto.getCommentId(), Boolean.TRUE)) {
            log.error("Comment not found!");
            throw new ItemNotFoundException(
                    "Comment not found!"
            );
        }
        entity.setCommentId(dto.getCommentId());

        commentRepository.save(entity);
    }

    private CommentEntity toEntity(CommentCreateDTO dto) {
        CommentEntity entity = new CommentEntity();
        entity.setContent(dto.getContent());
        entity.setProfileId(dto.getProfileId());
        entity.setVideoId(dto.getVideoId());

        return entity;
    }

    public void update(Integer id, CommentUpdateDTO dto) {
        CustomUserDetails user = profileService.getCurrentUser();

        Optional<CommentEntity> optional =
                commentRepository.findByIdAndVisible(
                        id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Comment not found!");
            throw new ItemNotFoundException(
                    "Comment not found!"
            );
        }

        CommentEntity entity = optional.get();

        if (!entity.getProfileId().equals(user.getProfile().getId())) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        entity.setContent(dto.getContent());

        commentRepository.update(entity.getContent(), LocalDateTime.now(), entity.getId());
    }

    public void delete(Integer id) {
        CustomUserDetails user = profileService.getCurrentUser();
        Optional<CommentEntity> optional =
                commentRepository.findByIdAndVisible(
                        id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Comment not found!");
            throw new ItemNotFoundException(
                    "Comment not found!"
            );
        }

        CommentEntity entity = optional.get();

        if (!entity.getProfileId().equals(user.getProfile().getId())
                && !user.getProfile().getRole().equals(ProfileRole.ROLE_ADMIN)) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        commentRepository.changeVisible(Boolean.FALSE, entity.getId());
    }

    public List<CommentDTO> pagination(int size, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CommentEntity> all = commentRepository.findAll(pageable);

        return toResponseList(all);
    }

    private List<CommentDTO> toResponseList(Page<CommentEntity> all) {
        List<CommentDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(entity.getId());
            dto.setContent(entity.getContent());
            dto.setProfileId(entity.getProfileId());
            dto.setVideoId(entity.getVideoId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdatedDate(entity.getUpdatedDate());
            dto.setVisible(entity.getVisible());

            dtoList.add(dto);
        });

        return dtoList;
    }

    private List<CommentDTO> toResponseList(List<CommentEntity> all) {
        List<CommentDTO> dtoList = new LinkedList<>();
        all.forEach(entity -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(entity.getId());
            dto.setContent(entity.getContent());
            dto.setProfileId(entity.getProfileId());
            dto.setVideoId(entity.getVideoId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdatedDate(entity.getUpdatedDate());
            dto.setVisible(entity.getVisible());

            dtoList.add(dto);
        });

        return dtoList;
    }

    public List<CommentDTO> list() {
        CustomUserDetails user = profileService.getCurrentUser();

        List<CommentEntity> entities = commentRepository.
                findAllByProfileId(user.getProfile().getId());

        if (entities.size() == 0) {
            log.error("No comments left by this profile yet");
            throw new ItemNotFoundException(
                    "No comments left by this profile yet"
            );
        }

        return toResponseList(entities);
    }

    public List<CommentInfoDTO> listByVideo(String id) {
        if (!videoService.exists(id)) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        List<CommentInfoRepository> list = commentRepository.findAllByVideoIdWithInfo(id);

        return toResponseListWithCommentInfo(list);
    }

    private List<CommentInfoDTO> toResponseListWithCommentInfo(List<CommentInfoRepository> list) {
        List<CommentInfoDTO> dtoList = new LinkedList<>();
        list.forEach(entity -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(entity.getCommentId());
            commentDTO.setContent(entity.getContent());
            commentDTO.setCreatedDate(entity.getCreatedDate());

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(entity.getProfileId());
            profileDTO.setUsername(entity.getUsername());

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(entity.getPhotoId());
            attachDTO.setUrl(attachService.getUrl(entity.getPhotoId()));

            profileDTO.setPhoto(attachDTO);

            CommentInfoDTO dto = new CommentInfoDTO();
            dto.setComment(commentDTO);
            dto.setProfile(profileDTO);

            dtoList.add(dto);
        });
        return dtoList;
    }

    public List<CommentInfoDTO> replyListByComment(Integer id) {
        if (!commentRepository.existsByIdAndVisible(id,Boolean.TRUE)) {
            log.error("Comment not found!");
            throw new ItemNotFoundException(
                    "Comment not found!"
            );
        }

        List<CommentInfoRepository> list = commentRepository.findAllByCommentIdWithInfo(id);

        return toResponseListWithCommentInfo(list);
    }

    public boolean exists(Integer commentId) {
        return commentRepository.existsByIdAndVisible(commentId,Boolean.TRUE);
    }
}
