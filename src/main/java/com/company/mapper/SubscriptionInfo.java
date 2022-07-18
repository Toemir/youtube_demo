package com.company.mapper;

import com.company.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;


public interface SubscriptionInfo {
    Integer getSubscriptionId();
    String getChannelId();
    String getChannelName();
    String getChannelPhotoId();
    NotificationType getNotificationType();

    LocalDateTime getCreatedDate();


//    SubscriptionInfo
//    id,channel(id,name,photo(id,url)),notification_type
}
