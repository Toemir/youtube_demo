package com.company.dto;

import com.company.enums.CommentLikeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentLikeCreateDTO {
    @NotNull(message = "Profile shouldn't be empty")
    private Integer profileId;
    @NotNull(message = "Comment shouldn't be empty")
    private Integer commentId;
}
