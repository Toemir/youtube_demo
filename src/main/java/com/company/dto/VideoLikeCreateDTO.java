package com.company.dto;

import com.company.enums.VideoLikeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class VideoLikeCreateDTO {
    @NotEmpty(message = "Video shouldn't be empty")
    private String videoId;
    @NotNull(message = "Profile shouldn't be empty")
    private Integer profileId;
}
