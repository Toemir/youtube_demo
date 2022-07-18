package com.company.repository;

import com.company.entity.video.VideoTagEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends PagingAndSortingRepository<VideoTagEntity, Integer> {
    boolean existsByTagIdAndVideoId(Integer tagId, String videoId);

    Optional<VideoTagEntity> findByVideoIdAndTagId(String videoId, Integer tagId);

    @Query("from VideoTagEntity where videoId=?1")
    List<VideoTagEntity> findAllByVideoId(String videoId);

    void deleteByVideoIdAndTagId(String videoId, Integer tagId);
}
