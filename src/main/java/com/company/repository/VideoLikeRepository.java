package com.company.repository;

import com.company.entity.video.VideoLikeEntity;
import com.company.enums.VideoLikeType;
import com.company.mapper.VideoLikeInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity,Integer> {

    boolean existsByVideoIdAndProfileId(String videoId, Integer profileId);

    @Modifying
    @Transactional
    @Query("delete from VideoLikeEntity where videoId=?1 and profileId=?2")
    void deleteByVideoIdAndProfileId(String videoId, Integer profileId);

    @Query(value = "select vl.id as videoLikeId," +
            " v.id as videoId,v.name as videoName," +
            " v.preview_id as previewId," +
            " c.id as channelId,c.name as channelName " +
            " from video_like vl " +
            " inner join video v " +
            " on vl.video_id = v.id " +
            " inner join channel c " +
            " on v.channel_id = c.id " +
            " inner join profile p " +
            " on p.id = c.profile_id " +
            " where p.email = :email " +
            " order by vl.created_date desc ",nativeQuery = true)
    List<VideoLikeInfo> listByProfileEmailWithVideoLikeInfo(@Param("email") String email);

    @Query(value = "select vl.id as videoLikeId," +
            " v.id as videoId,v.name as videoName," +
            " v.preview_id as previewId," +
            " c.id as channelId,c.name as channelName " +
            " from video_like vl " +
            " inner join video v " +
            " on vl.video_id = v.id " +
            " inner join channel c " +
            " on v.channel_id = c.id " +
            " where vl.profile_id = :profileId ",nativeQuery = true)
    List<VideoLikeInfo> listByProfileIdWithVideoLikeInfo(@Param("profileId") Integer profileId);

    Optional<VideoLikeEntity> findByProfileIdAndVideoId(Integer profileId, String videoId);
}
