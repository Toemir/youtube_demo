package com.company.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfo {
    String getPlaylistId();
    String getVideoId();
    String getVideoName();
    String getPreviewId();
    String getName();
    String getChannelId();
    String getChannelName();
    LocalDateTime getCreatedDate();
    Integer getOrderNumber();









    //    playlist_id,video(id,preview_attach(id,key,url),title,duration),
//    channel(id,name,key),created_date, order_num
}
