package com.company.dto;

import com.company.dto.video.VideoDTO;
import com.company.dto.video.VideoLikeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoLikeInfoDTO {
    private VideoLikeDTO videoLike;
    private VideoDTO video;
}
