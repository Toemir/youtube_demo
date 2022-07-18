package com.company.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentUpdateDTO {
    @NotEmpty(message = "Content is required")
    private String content;
}
