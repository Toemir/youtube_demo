package com.company.mapper;

public interface VideoLikeInfo {
    Integer getVideoLikeId();

    String getVideoId();
    String getVideoName();
    String getPreviewId();

    String getChannelId();
    String getChannelName();

//    VideoLikeInfo
//    id,video(id,name,key,channel(id,name),duration),preview_attach(id,url)
}
