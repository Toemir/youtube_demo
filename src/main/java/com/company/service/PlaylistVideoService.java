package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.channel.ChannelDTO;
import com.company.dto.playlist.PlaylistDTO;
import com.company.dto.playlist.PlaylistVideoDTO;
import com.company.dto.playlist.PlaylistVideoDeleteDTO;
import com.company.dto.playlist.PlaylistVideoInfoDTO;
import com.company.dto.video.VideoDTO;
import com.company.entity.video.PlaylistVideoEntity;
import com.company.entity.video.VideoEntity;
import com.company.exceptions.ItemNotFoundException;
import com.company.mapper.ChannelVideoInfo;
import com.company.mapper.PlaylistVideoInfo;
import com.company.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlaylistVideoService {
    private final PlaylistVideoRepository playlistVideoRepository;

    private final VideoService videoService;

    private final AttachService attachService;

    @Lazy
    @Autowired
    private PlaylistService playlistService;

    private final ProfileService profileService;


    public PlaylistVideoService(PlaylistVideoRepository playlistVideoRepository, VideoService videoService, AttachService attachService, ProfileService profileService) {
        this.playlistVideoRepository = playlistVideoRepository;
        this.videoService = videoService;
        this.attachService = attachService;
        this.profileService = profileService;
    }

    public String create(PlaylistVideoDTO dto) {
        CustomUserDetails user = profileService.getCurrentUser();

        if (!videoService.exists(dto.getVideoId())) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        if (!playlistService.exists(dto.getPlaylistId())) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        PlaylistVideoEntity entity = toEntity(dto);

        playlistVideoRepository.save(entity);

        return "Successfully saved a playlist video";
    }

    private PlaylistVideoEntity toEntity(PlaylistVideoDTO dto) {
        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setVideoId(dto.getVideoId());
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setOrder(dto.getOrder());

        return entity;
    }

    public String update(PlaylistVideoDTO dto) {
        Optional<PlaylistVideoEntity> optional = playlistVideoRepository.
                findByPlaylistIdAndVideoIdAndVisible(dto.getPlaylistId(),
                        dto.getVideoId(), Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Playlist video not found!");
            throw new ItemNotFoundException(
                    "Playlist video not found!"
            );
        }

        PlaylistVideoEntity entity = optional.get();
        entity.setVideoId(dto.getVideoId());
        entity.setPlaylistId(dto.getPlaylistId());
        entity.setOrder(dto.getOrder());

        playlistVideoRepository.save(entity);

        return "Successfully updated a playlist video";
    }

    public String delete(PlaylistVideoDeleteDTO dto) {
        Optional<PlaylistVideoEntity> optional = playlistVideoRepository.
                findByPlaylistIdAndVideoIdAndVisible(dto.getPlaylistId(),
                        dto.getVideoId(), Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Playlist video not found!");
            throw new ItemNotFoundException(
                    "Playlist video not found!"
            );
        }

        playlistVideoRepository.changeStatus(
                Boolean.FALSE,dto.getPlaylistId(),dto.getVideoId());

        return "Successfully deleted a playlist video";
    }

    public List<PlaylistVideoInfoDTO> list(String playlistId) {
        if (!playlistService.exists(playlistId)) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        List<PlaylistVideoInfo> list =
                playlistVideoRepository.
                        findAllByPlaylistIdWithPlaylistInfo(
                                playlistId, Boolean.TRUE);

        List<PlaylistVideoInfoDTO> dtoList = new LinkedList<>();

        list.forEach(entity -> {
            PlaylistVideoInfoDTO dto = toDTO(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }

    private PlaylistVideoInfoDTO toDTO(PlaylistVideoInfo entity) {
        PlaylistVideoInfoDTO dto = new PlaylistVideoInfoDTO();

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(entity.getVideoId());
        videoDTO.setName(entity.getVideoName());
        videoDTO.setPreviewId(entity.getPreviewId());
        videoDTO.setPreviewUrl(attachService.getUrl(entity.getPreviewId()));

        dto.setVideo(videoDTO);

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setId(entity.getPlaylistId());

        dto.setPlaylist(playlistDTO);

        PlaylistVideoDTO playlistVideoDTO = new PlaylistVideoDTO();
        playlistVideoDTO.setCreatedDate(entity.getCreatedDate());
        playlistVideoDTO.setOrder(entity.getOrderNumber());

        dto.setPlaylistVideo(playlistVideoDTO);

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(entity.getChannelId());
        channelDTO.setName(entity.getChannelName());

        dto.setChannel(channelDTO);

        return dto;
    }

    public List<VideoDTO> getVideoListByPlaylistId(String playlistId) {
        List<VideoEntity> list = playlistVideoRepository.findTop2ByPlaylistId(playlistId);

        List<VideoDTO> dtoList = new LinkedList<>();
        list.forEach(entity -> {
            VideoDTO dto = new VideoDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            dtoList.add(dto);
        });

        return dtoList;
    }
}
