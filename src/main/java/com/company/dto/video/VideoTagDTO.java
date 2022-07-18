package com.company.dto.video;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class VideoTagDTO {
    private Integer id;
    @NotEmpty(message = "Video shouldn't be empty")
    private String videoId;
    @NotNull(message = "Tag shouldn't be empty")
    private Integer tagId;
    private LocalDateTime createdDate;
}
