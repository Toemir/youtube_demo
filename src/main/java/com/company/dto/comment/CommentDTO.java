package com.company.dto.comment;

import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.video.VideoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;

    private String content;

    private Integer profileId;

    private Integer commentId;

    private String videoId;

    private Boolean visible;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
