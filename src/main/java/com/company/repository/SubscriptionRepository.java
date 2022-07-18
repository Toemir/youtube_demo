package com.company.repository;

import com.company.entity.SubscriptionEntity;
import com.company.enums.NotificationType;
import com.company.enums.SubscriptionStatus;
import com.company.mapper.SubscriptionInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionRepository extends PagingAndSortingRepository<SubscriptionEntity,Integer> {

    @Modifying
    @Transactional
    @Query("update SubscriptionEntity set status=?1 where channelId=?2 and profileId=?3")
    void changeStatus(SubscriptionStatus status, String channelId, Integer profileId);

    boolean existsByChannelIdAndProfileIdAndStatus(String channelId,
                                                   Integer profileId,
                                                   SubscriptionStatus status);

    @Modifying
    @Transactional
    @Query("update SubscriptionEntity set type=?1 where channelId=?2 and profileId=?3")
    void changeNotificationTypeByChannelIdAndProfileId(NotificationType type, String channelId, Integer profileId);


    @Query(value = "select s.id as subscriptionId, s.type as notificationType, " +
            "             c.id as channelId, c.name as channelName, " +
            "             c.photo_id as channelPhotoId " +
            "             from subscription as s " +
            "             inner join channel as c " +
            "             on c.id = s.channel_id " +
            "             inner join profile as p " +
            "             on s.profile_id = p.id " +
            "             where p.id = :id " +
            "             and s.status = 'ACTIVE' ",nativeQuery = true)
    List<SubscriptionInfo> listByProfileIdWithSubscriptionInfo(@Param("id") Integer profileId);

    @Query(value = "select s.id as subscriptionId, s.type as notificationType," +
            "             s.created_date as createdDate, " +
            "             c.id as channelId, c.name as channelName, " +
            "             c.photo_id as channelPhotoId " +
            "             from subscription as s " +
            "             inner join channel as c " +
            "             on c.id = s.channel_id " +
            "             inner join profile as p " +
            "             on s.profile_id = p.id " +
            "             where p.id = :id " +
            "             and s.status = 'ACTIVE' ",nativeQuery = true)
    List<SubscriptionInfo> listByProfileIdForAdminWithSubscriptionInfo(@Param("id") Integer profileId);

}
