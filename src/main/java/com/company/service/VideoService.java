package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.*;
import com.company.dto.channel.ChannelDTO;
import com.company.dto.playlist.PlaylistVideoDTO;
import com.company.dto.video.*;
import com.company.entity.TagEntity;
import com.company.entity.video.VideoEntity;
import com.company.enums.ProfileRole;
import com.company.enums.VideoStatus;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.MethodNotAllowedException;
import com.company.mapper.ChannelVideoInfo;
import com.company.mapper.VideoFullInfo;
import com.company.repository.VideoRepository;
import com.company.mapper.VideoShortInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;


    @Autowired
    @Lazy
    private VideoLikeService videoLikeService;

    @Autowired
    @Lazy
    private AttachService attachService;

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Autowired
    @Lazy
    private TagService tagService;

    @Autowired
    @Lazy
    private ChannelService channelService;

    @Autowired
    @Lazy
    private PlaylistService playlistService;

    @Autowired
    @Lazy
    private PlaylistVideoService playlistVideoService;

    @Autowired
    @Lazy
    private VideoTagService videoTagService;



    private final ProfileService profileService;

    public VideoService(VideoRepository videoRepository, ProfileService profileService) {
        this.videoRepository = videoRepository;
        this.profileService = profileService;
    }

    public boolean existsPhoto(String id) {
        return videoRepository.existsByAttachIdOrPreviewId(id,id);
    }

    public String create(VideoDTO dto) {
        existsDTOParameters(dto);

        VideoEntity entity = toEntity(dto);

        videoRepository.save(entity);

        createPlaylistVideo(entity,dto);


        dto.getTagList().forEach(tagName -> {
            TagEntity tagEntity = tagService.find(tagName);

            if (tagEntity == null) {
                TagDTO tagDTO = new TagDTO();
                tagDTO.setName(tagName);
                tagService.create(tagDTO);

                createVideoTag(tagName,entity.getId());
            } else {
                createVideoTag(tagName,entity.getId());
            }
        });

        return "Successfully saved a video";
    }

    private void createVideoTag(String tagName, String videoId) {
        TagEntity entity = tagService.find(tagName);

        VideoTagDTO dto = new VideoTagDTO();
        dto.setTagId(entity.getId());
        dto.setVideoId(videoId);

        videoTagService.save(dto);
    }

    private void existsDTOParameters(VideoDTO dto) {
        if (!attachService.exists(dto.getAttachId())) {
            log.error("Attach not found!");
            throw new ItemNotFoundException(
                    "Attach not found!"
            );
        }

        if (!attachService.exists(dto.getPreviewId())) {
            log.error("Preview not found!");
            throw new ItemNotFoundException(
                    "Preview not found!"
            );
        }

        if (!channelService.exists(dto.getChannelId())) {
            log.error("Channel not found!");
            throw new ItemNotFoundException(
                    "Channel not found!"
            );
        }

        if (!playlistService.exists(dto.getPlaylistId())) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        dto.getCategoryList().forEach(categoryId -> {
            if (!categoryService.exists(categoryId)) {
                log.error("Category not found!");
                throw new ItemNotFoundException(
                        "Category not found!"
                );
            }
        });
    }



    private void createPlaylistVideo(VideoEntity entity,VideoDTO dto) {
        PlaylistVideoDTO playlistVideoDTO = new PlaylistVideoDTO();
        playlistVideoDTO.setVideoId(entity.getId());
        playlistVideoDTO.setPlaylistId(dto.getPlaylistId());
        playlistVideoDTO.setOrder(dto.getOrder());

        playlistVideoService.create(playlistVideoDTO);
    }

    private VideoEntity toEntity(VideoDTO dto) {
        VideoEntity entity = new VideoEntity();
        entity.setAttachId(dto.getAttachId());
        entity.setPreviewId(dto.getPreviewId());
        entity.setChannelId(dto.getChannelId());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());

        return entity;
    }

    public boolean exists(String videoId) {
        return videoRepository.existsById(videoId);
    }

    public int getCount(Integer id) {
        return videoRepository.getCount(id);
    }

    public String update(VideoUpdateDTO dto, String id) {
        Optional<VideoEntity> optional =
                videoRepository.findByIdAndVisible(
                        id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        VideoEntity entity = optional.get();

        if (!profileService.getCurrentUser()
                .getProfile().getId().equals(
                        entity.getChannel().getProfileId())) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        videoRepository.update(entity.getName(),
                entity.getDescription(),entity.getId());

        return "Successfully updated a video";

    }

    public String changeStatus(VideoStatusDTO dto, String id) {
        CustomUserDetails user = profileService.getCurrentUser();

        Optional<VideoEntity> optional = videoRepository.
                findByIdAndVisible(id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        VideoEntity entity = optional.get();

        if (!user.getProfile().getId().equals(
                entity.getChannel().getProfileId())) {
            log.error("Method not allowed!");
            throw new ItemNotFoundException(
                    "Method not allowed!"
            );
        }

        entity.setStatus(dto.getStatus());

        videoRepository.changeStatus(entity.getStatus(),entity.getId());

        return "Successfully changed video's status";
    }

//    public String increaseViewCount(String id) {
//        if (!videoRepository.existsByIdAndVisible(id,Boolean.TRUE)) {
//            log.error("Video not found!");
//            throw new ItemNotFoundException(
//                    "Video not found!"
//            );
//        }
//
//        videoRepository.increaseViewCount(id);
//
//        return "Successfully increased video's view count";
//    }

    public List<VideoShortInfoDTO> paginationByCategory(Integer categoryId, int size, int page) {
        if (!categoryService.exists(categoryId)) {
            log.error("Category not found!");
            throw new ItemNotFoundException(
                    "Category not found!"
            );
        }

        List<VideoShortInfo> all =
                videoRepository.paginationByCategoryWithShortInfo(
                        categoryId,size,page);

        return responseListWithShortInfo(all);
    }

    private VideoShortInfoDTO toDTOWithShortInfo(VideoShortInfo entity) {
        VideoShortInfoDTO dto = new VideoShortInfoDTO();
        dto.setVideoId(entity.getVideoId());
        dto.setVideoName(entity.getVideoName());
        dto.setVideoPreviewId(entity.getPreviewId());
        dto.setVideoCreatedDate(entity.getCreatedDate());

        dto.setChannelId(entity.getChannelId());
        dto.setChannelPhotoId(entity.getPhotoId());
        dto.setChannelName(entity.getChannelName());

        dto.setViewCount(entity.getViewCount());

        return dto;
    }

    public List<VideoShortInfoDTO> getByName(String name) {
        List<VideoShortInfo> list =
                videoRepository.getByNameWithShortInfo(name);

        if (list.size() == 0) {
            log.error("Video not found");
            throw new ItemNotFoundException(
                    "Video not found"
            );
        }


        return responseListWithShortInfo(list);
    }

    public List<VideoShortInfoDTO> responseListWithShortInfo(
            List<VideoShortInfo> list) {
        List<VideoShortInfoDTO> dtoList = new LinkedList<>();

        list.forEach(entity -> {
            VideoShortInfoDTO dto = toDTOWithShortInfo(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }

    public List<VideoShortInfoDTO> paginationByTag(Integer id, int size, int page) {
        if (!tagService.exists(id)) {
            log.error("Tag not found!");
            throw new ItemNotFoundException(
                    "Tag not found!"
            );
        }

        List<VideoShortInfo> all =
                videoRepository.paginationByTagWithShortInfo(
                        id,size,page);

        return responseListWithShortInfo(all);
    }


    public VideoFullInfoDTO getById(String id) {
        CustomUserDetails user = profileService.getCurrentUser();

        Optional<VideoEntity> optional
                = videoRepository.findByIdAndVisible(id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Video not found!");
            throw new ItemAlreadyExistsException(
                    "Video not found!"
            );
        }

        VideoEntity video = optional.get();

        if (video.getStatus().equals(VideoStatus.PRIVATE) &&
                !user.getProfile().getRole().equals(ProfileRole.ROLE_ADMIN)) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        VideoFullInfo entity = videoRepository.getByIdWithFullInfo(id,user.getProfile().getId());

        return toDTOWithFullInfo(entity);
    }

    private VideoFullInfoDTO toDTOWithFullInfo(
            VideoFullInfo entity) {
        VideoFullInfoDTO dto = new VideoFullInfoDTO();

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(entity.getVideoId());
        videoDTO.setName(entity.getVideoName());
        videoDTO.setDescription(entity.getVideoDescription());
        videoDTO.setViewCount(entity.getViewCount());
        videoDTO.setSharedCount(entity.getSharedCount());
        videoDTO.setCreatedDate(entity.getCreatedDate());

        dto.setVideo(videoDTO);

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(entity.getChannelId());
        channelDTO.setName(entity.getChannelName());
        channelDTO.setPhotoId(entity.getChannelPhotoId());
        channelDTO.setPhotoUrl(attachService.getUrl(channelDTO.getPhotoId()));

        dto.setChannel(channelDTO);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(entity.getCategoryId());
        categoryDTO.setName(entity.getCategoryName());

        dto.setCategory(categoryDTO);

        AttachDTO preview = new AttachDTO();
        preview.setId(entity.getPreviewId());
        preview.setUrl(attachService.getUrl(preview.getId()));

        dto.setPreview(preview);

        AttachDTO attach = new AttachDTO();
        attach.setId(entity.getAttachId());
        attach.setUrl(attachService.getUrl(attach.getId()));

        dto.setAttach(attach);

        List<TagDTO> tagList = tagService.getTagListByVideoId(entity.getVideoId());

        dto.setTagList(tagList);

        LikeDTO like = new LikeDTO();
        like.setLikeCount(entity.getVideoLikeCount());
        like.setDislikeCount(entity.getVideoDislikeCount());
        like.setType(entity.getIsUserLiked());

        dto.setLike(like);

        return dto;
    }

    public List<ChannelVideoInfoDTO> getByPlaylistVideoList(String id,int size,int page) {
        if (!channelService.exists(id)) {
            log.error("Channel not found!");
            throw new ItemNotFoundException(
                    "Channel not found!"
            );
        }

        List<ChannelVideoInfo> list = videoRepository.getChannelVideoListWithPagination(id,size,page);

        return toResponseListWithChannelVideoInfo(list);
    }

    private List<ChannelVideoInfoDTO> toResponseListWithChannelVideoInfo(List<ChannelVideoInfo> list) {
        List<ChannelVideoInfoDTO> dtoList = new LinkedList<>();

        list.forEach(entity -> {
            ChannelVideoInfoDTO dto = new ChannelVideoInfoDTO();

            VideoDTO video = new VideoDTO();
            video.setId(entity.getVideoId());
            video.setName(entity.getVideoName());
            video.setViewCount(entity.getViewCount());
            video.setCreatedDate(entity.getCreatedDate());

            AttachDTO preview = new AttachDTO();
            preview.setId(entity.getPreviewId());
            preview.setUrl(attachService.getUrl(preview.getId()));

            dto.setVideo(video);
            dto.setPreview(preview);

            dtoList.add(dto);
        });

        return dtoList;
    }
}
