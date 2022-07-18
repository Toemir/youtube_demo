package com.company.repository;

import com.company.entity.video.PlaylistVideoEntity;
import com.company.entity.video.VideoEntity;
import com.company.mapper.PlaylistVideoInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity,String> {
    Optional<PlaylistVideoEntity>
    findByPlaylistIdAndVideoIdAndVisible(String playlistId,
                                         String videoId,
                                         Boolean visible);

    @Modifying
    @Transactional
    @Query("update PlaylistVideoEntity set visible=?1 where playlistId=?2 and visible=?3 ")
    void changeStatus(Boolean visible, String playlistId, String videoId);


    @Query(value = "select p.id as playlistId,v.id as videoId,v.preview_id as previewId, " +
            " v.name as videoName, c.id as channelId,c.name as channelName, " +
            " pv.order_number as orderNumber, " +
            " pv.created_date as createdDate " +
            " from playlist_video pv " +
            " inner join playlist p " +
            " on pv.playlist_id=p.id " +
            " inner join video v " +
            " on pv.video_id=v.id " +
            " inner join channel c " +
            " on v.channel_id=c.id " +
            " where p.id = ?1 and p.visible = ?2",nativeQuery = true)
    List<PlaylistVideoInfo> findAllByPlaylistIdWithPlaylistInfo(String playlistId,
                                                                Boolean visible);

    @Query("select new VideoEntity(v.id,v.name) " +
            " from VideoEntity v " +
            " inner join PlaylistVideoEntity pv " +
            " on v.id=pv.videoId " +
            " where pv.playlistId=?1 ")
    List<VideoEntity> findTop2ByPlaylistId(String playlistId);

    //    playlist_id,video(id,preview_attach(id,key,url),title,duration),
//    channel(id,name,key),created_date, order_num
}
