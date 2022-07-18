package com.company.repository;

import com.company.entity.video.VideoEntity;
import com.company.enums.VideoStatus;
import com.company.mapper.ChannelVideoInfo;
import com.company.mapper.VideoFullInfo;
import com.company.mapper.VideoShortInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
public interface VideoRepository extends PagingAndSortingRepository<VideoEntity,String> {
    boolean existsByAttachIdOrPreviewId(String attachId, String previewId);

    @Query("select count(v.id) " +
            " from VideoEntity v " +
            " inner join ChannelEntity c " +
            " on v.channelId=c.id " +
            " where c.profileId=:id")
    int getCount(@Param("id") Integer id);

    Optional<VideoEntity> findByIdAndVisible(String id, Boolean visible);

    @Modifying
    @Transactional
    @Query("update VideoEntity set name=:name,description=:description " +
            " where id=:id")
    void update(@Param("name") String name,
                @Param("description") String description,
                @Param("id") String id);


    @Modifying
    @Transactional
    @Query("update VideoEntity set status=?1 where id=?2")
    void changeStatus(VideoStatus status, String id);

    boolean existsByIdAndVisible(String id, Boolean visible);

    @Query(value = "select v.id as videoId,v.name as videoName,v.preview_id as previewId, " +
            " v.created_date as createdDate,v.channel_id as channelId, " +
            " c.name as channelName,c.photo_id as photoId, " +
            " count() as viewCount " +
            " from video AS v " +
            " inner join channel c " +
            " on c.id = v.channel_id " +
            " v.category_id=:id " +
            " group by v.id,v.name,v.preview_id,v.created_date, " +
            " v.channel_id,c.name,c.photo_id, c.id, " +
            " limit :limit " +
            " offset :offset",nativeQuery = true)
    List<VideoShortInfo> paginationByCategoryWithShortInfo(
            @Param("id") Integer categoryId,
            @Param("limit") int limit,
            @Param("offset") int offset);


    @Query(value = "select v.id as videoId,v.name as videoName,v.preview_id as previewId, " +
            " v.created_date as createdDate,v.channel_id as channelId, " +
            " c.name as channelName,c.photo_id as photoId, " +
            " sum() as viewCount " +
            " from video v " +
            " inner join channel c " +
            " on c.id = v.channel_id " +
            " where v.name LIKE %:name% " +
            " group by v.id,v.name,v.preview_id,v.created_date, " +
            " v.channel_id,c.name,c.photo_id, c.id, " +
            "  " ,nativeQuery = true)
    List<VideoShortInfo> getByNameWithShortInfo(@Param("name") String name);


    @Query(value = "select v.id as videoId,v.name as videoName, " +
            " v.preview_id as previewId, " +
            " v.created_date as createdDate,v.channel_id as channelId, " +
            " c.name as channelName,c.photo_id as photoId, " +
            " sum() as viewCount " +
            " from video v " +
            " inner join video_tag vt " +
            " on v.id=vt.video_id " +
            " inner join channel c " +
            " on c.id = v.channel_id " +
            " where vt.tag_id=:id " +
            " group by v.id,v.name,v.preview_id,v.created_date, " +
            " v.channel_id,c.name,c.photo_id, c.id, " +
            " ,cv.category_id " +
            " limit :limit offset :offset",nativeQuery = true)
    List<VideoShortInfo>
    paginationByTagWithShortInfo(@Param("id") Integer id,
                                 @Param("limit") int limit,
                                 @Param("offset") int offset);

    @Query(value = "select v.id as videoId, v.name as videoName," +
            " v.description as description, " +
            " v.created_date as createdDate," +
            " v.view_count as viewCount, v.shared_count as sharedCount, " +
            " v.preview_id as previewId," +
            " v.attach_id as attachId, " +
            " c.id as categoryId, c.name as categoryName, " +
            " ch.id as channelId, ch.name as channelName, ch.photo_id as channelPhotoId, " +
            " (select count(*) from video_like  where video_id =:videoId and type = 'LIKE') " +
            " as videoLikeCount, " +
            " (select count(*) from video_like  where video_id =:videoId and type = 'DISLIKE' ) " +
            " as videoDislikeCount, " +
            " (select type from video_like  where video_id =:videoId and profile_id=:profileId )" +
            " as isUserLiked " +
            " from video as v " +
            " inner join attach as a on a.id = v.attach_id " +
            " inner join category as c on c.id = v.category_id " +
            " inner join channel as ch on ch.id = v.channel_id " +
            " where v.id=:videoId ", nativeQuery = true)
    VideoFullInfo getByIdWithFullInfo(@Param("videoId") String videoId,
                                      @Param("profileId") Integer profileId);

    @Query(value = "select v.id as videoId,v.name as videoName," +
            " v.preview_id as previewId,  " +
            " v.created_date as createdDate " +
            " from video v " +
            " where v.channel_id=?1 " +
            " limit ?2 " +
            " offset ?3 ",nativeQuery = true)
    List<ChannelVideoInfo> getChannelVideoListWithPagination(String id, int size, int page);

}
