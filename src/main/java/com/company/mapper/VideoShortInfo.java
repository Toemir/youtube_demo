package com.company.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfo {
    String getVideoId();
    String getVideoName();
    String getPreviewId();
    LocalDateTime getCreatedDate();

    String getChannelId();
    String getChannelName();
    String getPhotoId();

    Integer getViewCount();
}
