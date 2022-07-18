package com.company.service;

import com.company.dto.CommentLikeCreateDTO;
import com.company.dto.CommentLikeDTO;
import com.company.entity.CommentLikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.CommentLikeType;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.CommentLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    @Autowired
    @Lazy
    private CommentService commentService;

    private final ProfileService profileService;

    public CommentLikeService(CommentLikeRepository commentLikeRepository, ProfileService profileService) {
        this.commentLikeRepository = commentLikeRepository;
        this.profileService = profileService;
    }

    public void like(CommentLikeCreateDTO dto) {
        likeOrDislike(dto.getCommentId(), dto.getProfileId(), CommentLikeType.LIKE);
    }

    public void dislike(CommentLikeCreateDTO dto) {
        likeOrDislike(dto.getCommentId(), dto.getProfileId(), CommentLikeType.DISLIKE);
    }

    private void likeOrDislike(Integer commentId, Integer pId, CommentLikeType type) {
        Optional<CommentLikeEntity> optional = commentLikeRepository.
                findByProfileIdAndCommentId(pId,commentId);
        if (optional.isPresent()) {
            CommentLikeEntity like = optional.get();
            like.setType(type);
            commentLikeRepository.save(like);
            return;
        }

        boolean articleExists = commentService.exists(commentId);
        if (!articleExists) {
            throw new ItemNotFoundException("Comment not found");
        }

        CommentLikeEntity like = new CommentLikeEntity();
        like.setCommentId(commentId);
        like.setProfileId(pId);
        like.setType(type);
        commentLikeRepository.save(like);
    }

    public void remove(Integer profileId,Integer commentId) {
        if (commentLikeRepository.existsByProfileIdAndCommentId(profileId,commentId)) {
            log.error("Comment like not found!");
            throw new ItemNotFoundException(
                    "Comment like not found!"
            );
        }

        commentLikeRepository.deleteByProfileIdAndCommentId(profileId,commentId);
    }

    public List<CommentLikeDTO> list() {
        ProfileEntity profile = profileService.getCurrentUser().getProfile();

        List<CommentLikeEntity> list =
                commentLikeRepository.findAllByProfileIdOrderByCreatedDateDesc(
                        profile.getId());

        if (list.size() == 0) {
            log.warn("This profile hasn't liked or disliked any comments yet");
            throw new BadRequestException(
                    "This profile hasn't liked or disliked any comments yet"
            );
        }

        return toResponseList(list);
    }

    public List<CommentLikeDTO> list(Integer profileId) {
        List<CommentLikeEntity> list =
                commentLikeRepository.findAllByProfileId(
                        profileId);

        if (list.size() == 0) {
            log.warn("This profile hasn't liked or disliked any comments yet");
            throw new BadRequestException(
                    "This profile hasn't liked or disliked any comments yet"
            );
        }

        return toResponseList(list);
    }

    private List<CommentLikeDTO> toResponseList(List<CommentLikeEntity> list) {
        List<CommentLikeDTO> dtoList = new LinkedList<>();

        list.forEach(entity -> {
            CommentLikeDTO dto = new CommentLikeDTO();
            dto.setId(entity.getId());
            dto.setProfileId(entity.getProfileId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setCommentId(entity.getCommentId());
            dto.setType(entity.getType());

            dtoList.add(dto);
        });

        return dtoList;
    }
}
