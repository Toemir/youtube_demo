package com.company.repository;

import com.company.entity.PlaylistEntity;
import com.company.enums.PlaylistStatus;
import com.company.mapper.PlayListShortInfo;
import com.company.mapper.PlaylistFullInfo;
import com.company.repository.CustomPlaylistRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface PlaylistRepository extends PagingAndSortingRepository<PlaylistEntity,String> {
    @Query(value = " select pe.id as playlistId,pe.name as playlistName, " +
            " pe.created_date as playlistCreatedDate, " +
            " c.id as channelId,c.name as channelName, " +
            " v.id as videoId,v.name as videoName, " +
            " count(v.id) as videoCount " +
            " from playlist_video pve " +
            " inner join playlist pe on pe.id=pve.playlist_id " +
            " inner join channel c on pe.channel_id=c.id " +
            " inner join video v on v.id = pve.video_id " +
            " inner join profile p on p.id = c.profile_id " +
            " where p.id = :id " +
            " group by pe.id, pe.name, pe.created_date, c.id, c.name, v.id, v.name,pve.order_number " +
            " order by pve.order_number desc",nativeQuery = true)
    List<PlayListShortInfo> findAllByProfileIdWithShortInfo(
            @Param("id") Integer id);

    @Query(value = " select pe.id as playlistId,pe.name as playlistName,pe.status as playlistStatus, " +
            " pe.order_number as playlistOrder, c.id as channelId,c.name as channelName," +
            " c.photo_id as channelPhotoId, " +
            " p.id as profileId,p.username as profileUsername," +
            " p.photo_id as profilePhotoId " +
            " from playlist pe " +
            " inner join channel c " +
            " on pe.channel_id=c.id " +
            " inner join profile p " +
            " on p.id=c.profile_id " +
            " where p.id = :id" +
            " group by pe.order_number,pe.id,pe.name,pe.status," +
            " c.id,c.name,c.photo_id,p.id," +
            " p.photo_id,p.username" +
            " order by pe.order_number desc",nativeQuery = true)
    List<PlaylistFullInfo> findAllByProfileIdWithFullInfo(@Param("id") Integer id);


    @Modifying
    @Transactional
    @Query("update PlaylistEntity set status=?1 where id=?2")
    void changeStatus(PlaylistStatus status, String id);

    boolean existsByIdAndVisible(String id, Boolean visible);

    Optional<PlaylistEntity> findByIdAndVisible(String id, Boolean visible);

    @Modifying
    @Transactional
    @Query("update PlaylistEntity set visible=?1 where id=?2")
    void changeVisible(Boolean visible, String id);

    @Query(value = "select pe.id as peId,pe.name as peName,pe.status as peStatus," +
            " pe.order_number as peOrder, c.id as cId,c.name as cName," +
            " c.photo_id as cPhotoId, " +
            " p.id as pId,p.username as pUsername," +
            " p.photo_id as pPhotoId, " +
            " count(pl.id) as videoCount" +
            " from playlist pe " +
            " inner join channel c " +
            " on pe.channel_id=c.id " +
            " inner join profile p " +
            " on p.id=c.profile_id " +
            " inner join playlist_video pl " +
            " on pe.id= pl.playlist_id " +
            " limit :limit " +
            " offset :offset ",nativeQuery = true)
    List<PlaylistFullInfo>
    findAllByProfileIdWithFullInfoWithPagination(@Param("offset") int page,
                                                 @Param("limit") int size);

    @Query(value = " select pe.id as playlistId,pe.name as playlistName, " +
            " pe.created_date as playlistCreateedDate, " +
            " c.id as channelId,c.name as channelName, " +
            " v.id as videoId,v.name as videoName," +
            " p.photo_id as profilePhotoId, " +
            " c.photo_id as channelPhotoId " +
            " from playlist_video pve " +
            " inner join playlist pe on pe.id=pve.playlist_id " +
            " inner join channel c on pve.channel_id=c.id " +
            " inner join video v on v.id = pve.video_id " +
            " inner join profile p on p.id = c.profile_id " +
            " where c.id = :id " +
            " group by pve.order_number," +
            " pe.name,pe.created_date, pe.name, pe.id, c.id, c.name, v.id, v.name," +
            " c.photo_id, p.photo_id " +
            " order by pve.order_number desc",nativeQuery = true)
    List<PlayListShortInfo> findAllByChannelIdWithShortInfo(@Param("id") String id);

    @Query(value = "SELECT p.id as playlistId, p.name as playlistName," +
            "  p.created_date as playlistCreatedDate," +
            "  c.id as channelId, c.name as channelName, " +
            "   (select count(*) from playlist_video  as pv where pv.playlist_id = p.id ) as videoCount, " +
            "   (select cast(count(*) as int) " +
            "       from profile_watched_video as  pwv " +
            "       inner join playlist_video as pv on pv.video_id = pwv.video_id " +
            "       where pv.playlist_id =:id ) as totalViewCount" +
            " from  playlist as p " +
            " inner join channel c " +
            " on c.id = p.channel_id " +
            " Where p.id = :id " +
            " and p.visible = true ", nativeQuery = true)
    Optional<PlayListShortInfo> getById(@Param("id") String id);
}
