package com.company.dto;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.video.VideoDTO;
import com.company.enums.CommentLikeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeDTO {
    private Integer id;
    private Integer profileId;
    private ProfileDTO profile;
    private Integer commentId;
    private ProfileDTO comment;
    private String videoId;
    private VideoDTO video;
    private CommentLikeType type;
    private LocalDateTime createdDate;
}
