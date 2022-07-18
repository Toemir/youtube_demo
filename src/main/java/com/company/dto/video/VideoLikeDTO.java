package com.company.dto.video;

import com.company.dto.profile.ProfileDTO;
import com.company.enums.VideoLikeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VideoLikeDTO {
    private Integer id;
    private Integer profileId;
    private ProfileDTO profile;
    private String videoId;
    private VideoDTO video;
    private VideoLikeType type;
    private LocalDateTime createdDate;

    public VideoLikeDTO(Integer id) {
        this.id = id;
    }
}
