package com.company.mapper;

import java.time.LocalDateTime;

public interface PlayListShortInfo {
    String getPlaylistId();
    String getPlaylistName();
    LocalDateTime getPlaylistCreatedDate();

    String getChannelId();
    String getChannelName();

    Integer getVideoCount();
    Integer getTotalViewCount();

}
