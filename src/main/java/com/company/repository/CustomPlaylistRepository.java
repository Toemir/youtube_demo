package com.company.repository;

import java.time.LocalDateTime;
public interface CustomPlaylistRepository {
    String getPeId();
    String getPeName();
    LocalDateTime getPeUpdatedDate();

    Integer getVideoCount();
    Integer getTotalViewCount();
}
