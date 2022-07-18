package com.company.service;

import com.company.dto.video.VideoTagDTO;
import com.company.entity.video.VideoTagEntity;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class VideoTagService {
    private final VideoTagRepository videoTagRepository;

    private final VideoService videoService;

    public VideoTagService(VideoTagRepository videoTagRepository, VideoService videoService) {
        this.videoTagRepository = videoTagRepository;
        this.videoService = videoService;
    }

    public void save(VideoTagDTO dto) {
        if (videoTagRepository.existsByTagIdAndVideoId(
                dto.getTagId(),dto.getVideoId())) {
            log.error("Video tag already exists!");
            throw new ItemAlreadyExistsException(
                    "Video tag already exists!"
            );
        }

        VideoTagEntity entity = toEntity(dto);

        videoTagRepository.save(entity);
    }

    private VideoTagEntity toEntity(VideoTagDTO dto) {
        VideoTagEntity entity = new VideoTagEntity();
        entity.setTagId(dto.getTagId());
        entity.setVideoId(dto.getVideoId());

        return entity;
    }

    public List<VideoTagDTO> list(String videoId) {
        if (!videoService.exists(videoId)) {
            log.error("Video not found!");
            throw new ItemNotFoundException(
                    "Video not found!"
            );
        }

        List<VideoTagEntity> list = videoTagRepository.findAllByVideoId(videoId);

        List<VideoTagDTO> dtoList = new LinkedList<>();

        list.forEach(videoTagEntity -> {
            VideoTagDTO dto = toDTO(videoTagEntity);

            dtoList.add(dto);
        });

        return dtoList;
    }

    private VideoTagDTO toDTO(VideoTagEntity entity) {
        VideoTagDTO dto = new VideoTagDTO();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setTagId(entity.getTagId());

        return dto;
    }

    public void delete(VideoTagDTO dto) {
        if (!videoTagRepository.existsByTagIdAndVideoId(
                dto.getTagId(),dto.getVideoId())) {
            log.error("Video tag not found!");
            throw new ItemNotFoundException(
                    "Video tag not found!"
            );
        }

        videoTagRepository.deleteByVideoIdAndTagId(dto.getVideoId(),dto.getTagId());
    }
}
