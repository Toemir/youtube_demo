package com.company.dto;

import com.company.dto.channel.ChannelDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.ChannelEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.NotificationType;
import com.company.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionDTO {
    private Integer id;
    private Integer profileId;
    private ProfileDTO profile;
    private String channelId;
    private ChannelDTO channel;
    private SubscriptionStatus status;
    private NotificationType type;
    private LocalDateTime createdDate;
}
