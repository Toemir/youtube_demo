package com.company.dto;

import com.company.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SubscriptionCreateDTO {
    @NotEmpty(message = "Channel shouldn't be empty")
    private String channelId;
    @NotNull(message = "Notification type shouldn't be empty")
    private NotificationType type;
}
