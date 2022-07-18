package com.company.dto;

import com.company.dto.channel.ChannelDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionInfoDTO {
    private SubscriptionDTO subscription;
}
