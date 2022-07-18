package com.company.dto.playlist;

import com.company.dto.video.VideoDTO;
import com.company.dto.channel.ChannelDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayListShortInfoDTO {
    private PlaylistDTO playlist;

    private ChannelDTO channel;

    private Integer videoCount;
    private Integer totalViewCount;

    private List<VideoDTO> videoList;
}
