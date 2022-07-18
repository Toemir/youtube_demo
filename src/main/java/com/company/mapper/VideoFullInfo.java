package com.company.mapper;

import com.company.enums.VideoLikeType;

import java.time.LocalDateTime;

public interface VideoFullInfo {
    String getVideoId();
    String getVideoName();
    String getVideoDescription();

    LocalDateTime getCreatedDate();

    String getPreviewId();
    String getAttachId();

    Integer getViewCount();
    Integer getSharedCount();

    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();

    Integer getCategoryId();
    String getCategoryName();

    Integer getVideoLikeCount();
    Integer getVideoDislikeCount();
    VideoLikeType getIsUserLiked();
}
