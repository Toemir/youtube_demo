package com.company.dto;

import com.company.dto.channel.ChannelDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.video.VideoDTO;
import com.company.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    private Integer id;
    private String content;
    private Integer profileId;
    private ProfileDTO profile;
    private String entityId;
    private ChannelDTO channel;
    private VideoDTO video;
    private EntityType type;
    private LocalDateTime createdDate;
}
