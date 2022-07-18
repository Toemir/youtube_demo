package com.company.dto;

import com.company.enums.NotificationType;
import com.company.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubscriptionUpdateStatusDTO {
    @NotEmpty(message = "Channel shouldn't be empty")
    private String channelId;
    @NotNull(message = "Status shouldn't be empty")
    private SubscriptionStatus status;
}
