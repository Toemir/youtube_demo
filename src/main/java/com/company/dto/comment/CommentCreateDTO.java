package com.company.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentCreateDTO {
    @NotNull(message = "Profile is required")
    private Integer profileId;

    @NotEmpty(message = "Video is required")
    private String videoId;

    @NotEmpty(message = "Content is required")
    private String content;

    private Integer commentId;
}
