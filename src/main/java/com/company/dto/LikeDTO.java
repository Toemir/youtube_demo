package com.company.dto;

import com.company.enums.VideoLikeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDTO {
    private Integer likeCount;
    private Integer dislikeCount;
    private VideoLikeType type;
}
