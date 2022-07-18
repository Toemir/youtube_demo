package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.*;
import com.company.dto.channel.ChannelDTO;
import com.company.dto.playlist.PlayListFullInfoDTO;
import com.company.dto.playlist.PlayListShortInfoDTO;
import com.company.dto.playlist.PlaylistDTO;
import com.company.dto.playlist.PlaylistStatusDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.video.VideoDTO;
import com.company.entity.PlaylistEntity;
import com.company.enums.ProfileRole;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.MethodNotAllowedException;
import com.company.exceptions.ProfileNotFoundException;
import com.company.mapper.PlayListShortInfo;
import com.company.mapper.PlaylistFullInfo;
import com.company.repository.CustomPlaylistRepository;
import com.company.repository.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlaylistService {
    @Value("${spring.server.url}")
    private String serverUrl;
    private final PlaylistRepository playlistRepository;

    private final VideoService videoService;

    @Lazy
    @Autowired
    private PlaylistVideoService playlistVideoService;

    private final ProfileService profileService;

    private final ChannelService channelService;

    public PlaylistService(PlaylistRepository playlistRepository, VideoService videoService, ProfileService profileService, ChannelService channelService) {
        this.playlistRepository = playlistRepository;
        this.videoService = videoService;
        this.profileService = profileService;
        this.channelService = channelService;
    }

    public String create(PlaylistDTO dto) {
        PlaylistEntity entity = toEntity(dto);

        playlistRepository.save(entity);

        return "Successfully saved a playlist";
    }

    private PlaylistEntity toEntity(PlaylistDTO dto) {
        PlaylistEntity entity = new PlaylistEntity();
        entity.setName(dto.getName());
        entity.setChannelId(dto.getChannelId());
        entity.setPreviewId(dto.getPreviewId());
        entity.setOrder(dto.getOrder());

        return entity;
    }

    public boolean exists(String id) {
        return playlistRepository.existsById(id);
    }

    public List<PlayListShortInfoDTO> listWithShortInfo(Integer id) {
        if (!profileService.exists(id)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<PlayListShortInfo> all = playlistRepository.
                findAllByProfileIdWithShortInfo(id);

        List<PlayListShortInfoDTO> dtoList = new LinkedList<>();

        all.forEach(playlistEntity -> {
            PlayListShortInfoDTO dto = playListShortInfoDTO(playlistEntity);
            dto.setVideoCount(all.size());

            dtoList.add(dto);
        });

        return dtoList;
    }

    private PlayListShortInfoDTO playListShortInfoDTO(PlayListShortInfo entity) {
        PlayListShortInfoDTO dto = new PlayListShortInfoDTO();
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setId(entity.getPlaylistId());
        playlistDTO.setName(entity.getPlaylistName());
        playlistDTO.setCreatedDate(entity.getPlaylistCreatedDate());

        dto.setPlaylist(playlistDTO);

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(entity.getChannelId());
        channelDTO.setName(entity.getChannelName());

        dto.setChannel(channelDTO);


        List<VideoDTO> videoList = playlistVideoService.
                getVideoListByPlaylistId(entity.getPlaylistId());

        dto.setVideoList(videoList);

        dto.setVideoCount(entity.getVideoCount());

        return dto;
    }

    public List<PlayListFullInfoDTO> listWithFullInfo(Integer id) {
        if (!profileService.exists(id)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<PlaylistFullInfo> all =
                playlistRepository.findAllByProfileIdWithFullInfo(id);

        List<PlayListFullInfoDTO> dtoList = new LinkedList<>();

        all.forEach(entity -> {
            PlayListFullInfoDTO dto = toDTO(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }


    public String update(PlaylistDTO dto,String id) {
        Optional<PlaylistEntity> optional =
                playlistRepository.findById(id);

        if (optional.isEmpty()) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        PlaylistEntity entity = optional.get();

        if (!profileService.getCurrentUser().
                getProfile().getId().
                equals(entity.getChannel().getProfileId())) {
            log.error("Method not allowed");
            throw new MethodNotAllowedException(
                    "Method not allowed"
            );
        }

        entity.setOrder(dto.getOrder());
        entity.setName(dto.getName());
        entity.setChannelId(dto.getChannelId());
        entity.setPreviewId(dto.getPreviewId());

        playlistRepository.save(entity);

        return "Successfully updated a playlist";
    }

    public String changeStatus(PlaylistStatusDTO dto, String id) {
        if (!playlistRepository.existsByIdAndVisible(id,Boolean.TRUE)) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        playlistRepository.changeStatus(dto.getStatus(),id);

        return "Successfully changed a playlist's status";


    }

    public String delete(String id) {
        Optional<PlaylistEntity> optional = playlistRepository.
                findByIdAndVisible(id, Boolean.TRUE);

        if (optional.isEmpty()) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        PlaylistEntity entity = optional.get();

        CustomUserDetails user = profileService.getCurrentUser();

        if (!user.getProfile().getId().equals(
                        entity.getChannel().getProfileId())
        && !user.getProfile().getRole().equals(ProfileRole.ROLE_ADMIN)) {
            log.error("Method not allowed!");
            throw new MethodNotAllowedException(
                    "Method not allowed!"
            );
        }

        playlistRepository.changeVisible(Boolean.FALSE,id);

        return "Successfully deleted a playlist";
    }

    public List<PlayListFullInfoDTO> pagination(int size, int page) {
        List<PlaylistFullInfo> all = playlistRepository.
                findAllByProfileIdWithFullInfoWithPagination(page,size);

        if (all == null) {
            log.error("No playlists yet");
            throw new ItemNotFoundException(
                    "No playlists yet"
            );
        }

        List<PlayListFullInfoDTO> dtoList = new LinkedList<>();

        all.forEach(playListFullInfoRepository -> {
            PlayListFullInfoDTO dto = toDTO(playListFullInfoRepository);

            dtoList.add(dto);
        });

        return dtoList;

    }

    public List<PlayListShortInfoDTO> listByChannel(String id) {
        if (!channelService.exists(id)) {
            log.error("Channel not found!");
            throw new ItemNotFoundException(
                    "Channel not found!"
            );
        }

        List<PlayListShortInfo> all = playlistRepository.findAllByChannelIdWithShortInfo(id);

        if (all==null) {
            log.error("No playlists created by this channel yet!");
            throw new ItemNotFoundException(
                    "No playlists created by this channel yet!"
            );
        }

        List<PlayListShortInfoDTO>dtoList = new LinkedList<>();

        all.forEach(entity -> {
            PlayListShortInfoDTO dto = toDTO(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }


    public PlayListShortInfoDTO getById(String id) {
        Optional<PlayListShortInfo> optional
                = playlistRepository.getById(id);

        if (optional.isEmpty()) {
            log.error("Playlist not found!");
            throw new ItemNotFoundException(
                    "Playlist not found!"
            );
        }

        PlayListShortInfo info = optional.get();

        return toDTO(info);
    }

    private PlayListShortInfoDTO toDTO(PlayListShortInfo info) {
        PlayListShortInfoDTO dto = new PlayListShortInfoDTO();

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setId(info.getPlaylistId());
        playlistDTO.setName(info.getPlaylistName());
        playlistDTO.setCreatedDate(info.getPlaylistCreatedDate());

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(info.getChannelId());
        channelDTO.setName(info.getChannelName());

        List<VideoDTO> videoList = playlistVideoService.
                getVideoListByPlaylistId(info.getPlaylistId());

        dto.setVideoCount(info.getVideoCount());
        dto.setTotalViewCount(info.getTotalViewCount());
        dto.setPlaylist(playlistDTO);
        dto.setChannel(channelDTO);
        dto.setVideoList(videoList);

        return dto;
    }

    private PlayListFullInfoDTO toDTO(PlaylistFullInfo entity) {
        PlayListFullInfoDTO dto = new PlayListFullInfoDTO();

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setId(entity.getPlaylistId());
        playlistDTO.setName(entity.getPlaylistName());
        playlistDTO.setStatus(entity.getPlaylistStatus());
        playlistDTO.setOrder(entity.getPlaylistOrder());

        AttachDTO channelPhoto = new AttachDTO();
        channelPhoto.setId(entity.getChannelPhotoId());
        channelPhoto.setUrl(serverUrl+"/api/v1/attach/"+entity.getChannelPhotoId());

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(entity.getChannelId());
        channelDTO.setName(entity.getChannelName());
        channelDTO.setPhoto(channelPhoto);

        AttachDTO profilePhoto = new AttachDTO();
        profilePhoto.setId(entity.getProfilePhotoId());
        profilePhoto.setUrl(serverUrl+"/api/v1/attach/"+entity.getProfilePhotoId());

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(entity.getProfileId());
        profileDTO.setUsername(entity.getProfileUsername());
        profileDTO.setPhoto(profilePhoto);

        dto.setPlaylist(playlistDTO);
        dto.setChannel(channelDTO);
        dto.setProfile(profileDTO);

        return dto;
    }
}
