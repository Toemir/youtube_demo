package com.company.mapper;

import java.time.LocalDateTime;

public interface ChannelVideoInfo {
    String getVideoId();
    String getVideoName();
    String getPreviewId();

    String getChannelId();
    String getChannelName();

    LocalDateTime getCreatedDate();

    Integer getViewCount();
}
