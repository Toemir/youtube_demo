package com.company.service;

import com.company.dto.VideoLikeCreateDTO;
import com.company.dto.VideoLikeInfoDTO;
import com.company.dto.VideoLikeRemoveDTO;
import com.company.dto.channel.ChannelDTO;
import com.company.dto.video.VideoDTO;
import com.company.dto.video.VideoLikeDTO;
import com.company.entity.video.VideoLikeEntity;
import com.company.enums.VideoLikeType;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.ProfileNotFoundException;
import com.company.mapper.VideoLikeInfo;
import com.company.repository.VideoLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;

    private final ProfileService profileService;

    private final AttachService attachService;

    private final VideoService videoService;

    public VideoLikeService(VideoLikeRepository videoLikeRepository, ProfileService profileService, AttachService attachService, VideoService videoService) {
        this.videoLikeRepository = videoLikeRepository;
        this.profileService = profileService;
        this.attachService = attachService;
        this.videoService = videoService;
    }

    public void remove(VideoLikeRemoveDTO dto) {
        if (!videoLikeRepository.existsByVideoIdAndProfileId(dto.getVideoId(),dto.getProfileId())) {
            log.error("This profile hasn't liked or disliked this video!");
            throw new BadRequestException(
                    "This profile hasn't liked or disliked this video!"
            );
        }

        videoLikeRepository.deleteByVideoIdAndProfileId(dto.getVideoId(),dto.getProfileId());
    }

    public void like(VideoLikeCreateDTO dto) {
        likeDislike(dto.getVideoId(), dto.getProfileId(), VideoLikeType.LIKE);
    }

    public void dislike(VideoLikeCreateDTO dto) {
        likeDislike(dto.getVideoId(), dto.getProfileId(), VideoLikeType.DISLIKE);
    }

    private void likeDislike(String videoId, Integer pId, VideoLikeType type) {
        Optional<VideoLikeEntity> optional = videoLikeRepository.
                findByProfileIdAndVideoId(pId,videoId);
        if (optional.isPresent()) {
            VideoLikeEntity like = optional.get();
            like.setType(type);
            videoLikeRepository.save(like);
            return;
        }

        boolean articleExists = videoService.exists(videoId);
        if (!articleExists) {
            throw new ItemNotFoundException("Video not found");
        }

        VideoLikeEntity like = new VideoLikeEntity();
        like.setVideoId(videoId);
        like.setProfileId(pId);
        like.setType(type);
        videoLikeRepository.save(like);
    }

    public List<VideoLikeInfoDTO> list() {
        String email = profileService.getCurrentUser().getUsername();

        if (!profileService.exists(email)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<VideoLikeInfo> list = videoLikeRepository.listByProfileEmailWithVideoLikeInfo(email);


        return toResponseListWithVideoLikeInfo(list);
    }

    public List<VideoLikeInfoDTO> list(Integer profileId) {
        if (!profileService.exists(profileId)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<VideoLikeInfo> list = videoLikeRepository.listByProfileIdWithVideoLikeInfo(profileId);


        return toResponseListWithVideoLikeInfo(list);
    }

    private List<VideoLikeInfoDTO> toResponseListWithVideoLikeInfo(List<VideoLikeInfo> list) {
        List<VideoLikeInfoDTO> dtoList = new LinkedList<>();

        list.forEach(info -> {
            VideoLikeInfoDTO dto = new VideoLikeInfoDTO();

            VideoLikeDTO videoLike = new VideoLikeDTO(info.getVideoLikeId());
            VideoDTO video = new VideoDTO();
            video.setId(info.getVideoId());
            video.setName(info.getVideoName());
            video.setPreviewId(info.getPreviewId());
            video.setPreviewUrl(attachService.getUrl(video.getPreviewId()));

            ChannelDTO channel = new ChannelDTO();
            channel.setId(info.getChannelId());
            channel.setName(info.getChannelName());

            video.setChannel(channel);

            dto.setVideo(video);
            dto.setVideoLike(videoLike);

            dtoList.add(dto);
        });

        return dtoList;
    }
}
