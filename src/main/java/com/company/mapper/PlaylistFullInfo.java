package com.company.mapper;

import com.company.enums.PlaylistStatus;

public interface PlaylistFullInfo {
    String getPlaylistId();
    String getPlaylistName();
    PlaylistStatus getPlaylistStatus();
    Integer getPlaylistOrder();

    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();

    Integer getProfileId();
    String getProfileUsername();
    String getProfilePhotoId();
}
