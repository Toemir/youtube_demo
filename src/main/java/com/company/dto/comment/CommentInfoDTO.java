package com.company.dto.comment;


import com.company.dto.profile.ProfileDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInfoDTO {
    private CommentDTO comment;
    private ProfileDTO profile;
}
