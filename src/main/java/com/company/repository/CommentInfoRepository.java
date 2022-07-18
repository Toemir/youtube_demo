package com.company.repository;

import java.time.LocalDateTime;

public interface CommentInfoRepository {
    Integer getCommentId();
    String getContent();
    LocalDateTime getCreatedDate();

    Integer getProfileId();
    String getUsername();
    String getPhotoId();
}
