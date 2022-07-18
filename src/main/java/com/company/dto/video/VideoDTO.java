package com.company.dto.video;

import com.company.dto.channel.ChannelDTO;
import com.company.enums.VideoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    private String id;

    @NotEmpty(message = "Name shouldn't be empty!")
    @Size(min = 1,max = 255,message = "Name's length should be between 1 and 255 characters!")
    private String name;

    @NotEmpty(message = "Description shouldn't be empty!")
    private String description;

    @NotEmpty(message = "Attach shouldn't be empty!")
    private String attachId;

    @NotEmpty(message = "Preview shouldn't be empty!")
    private String previewId;

    private String previewUrl;

    @NotEmpty(message = "Channel shouldn't be empty!")
    private String channelId;

    @NotEmpty(message = "Playlist shouldn't be empty!")
    private String playlistId;

    @NotNull(message = "Category shouldn't be empty!")
    private List<Integer> categoryList;

    @NotNull(message = "Tag shouldn't be empty!")
    private List<String> tagList;



    private Integer order;
    private Integer viewCount;

    private Integer sharedCount;
    private VideoStatus status;
    private Boolean visible;
    private LocalDateTime createdDate;

    private ChannelDTO channel;

    private Long duration;
}
