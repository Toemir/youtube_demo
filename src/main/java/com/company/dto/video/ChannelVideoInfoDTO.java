package com.company.dto.video;

import com.company.dto.AttachDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelVideoInfoDTO {
    private VideoDTO video;
    private AttachDTO preview;
}
