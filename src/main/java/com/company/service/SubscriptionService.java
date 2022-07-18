package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.*;
import com.company.dto.channel.ChannelDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.SubscriptionEntity;
import com.company.enums.SubscriptionStatus;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.ProfileNotFoundException;
import com.company.mapper.SubscriptionInfo;
import com.company.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    private final ProfileService profileService;

    private final AttachService attachService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, ProfileService profileService, AttachService attachService) {
        this.subscriptionRepository = subscriptionRepository;
        this.profileService = profileService;
        this.attachService = attachService;
    }

    public void create(SubscriptionCreateDTO dto) {
        CustomUserDetails user = profileService.getCurrentUser();

        ProfileEntity profile = profileService.get(user.getUsername());

        if (subscriptionRepository.existsByChannelIdAndProfileIdAndStatus(
                dto.getChannelId(),profile.getId(), SubscriptionStatus.ACTIVE)) {
            log.warn("This profile is already subscribed to this video!");
            throw new BadRequestException(
                    "This profile is already subscribed to this video!"
            );
        }

        SubscriptionEntity entity = toEntity(dto,profile.getId());

        subscriptionRepository.save(entity);
    }

    public void changeStatus(SubscriptionUpdateStatusDTO dto) {
        CustomUserDetails user = profileService.getCurrentUser();

        ProfileEntity profile = profileService.get(user.getUsername());

        if (!subscriptionRepository.existsByChannelIdAndProfileIdAndStatus(
                dto.getChannelId(),profile.getId(),
                SubscriptionStatus.ACTIVE)) {
            log.error("Subscription not found!");
            throw new ItemNotFoundException(
                    "Subscription not found!"
            );
        }

        subscriptionRepository.changeStatus(dto.getStatus(),dto.getChannelId(),profile.getId());
    }

    public void changeNotificationType(SubscriptionUpdateNotificationTypeDTO dto) {
        CustomUserDetails user = profileService.getCurrentUser();

        ProfileEntity profile = profileService.get(user.getUsername());

        if (!subscriptionRepository.existsByChannelIdAndProfileIdAndStatus(
                dto.getChannelId(),profile.getId(),
                SubscriptionStatus.ACTIVE)) {
            log.error("Subscription not found!");
            throw new ItemNotFoundException(
                    "Subscription not found!"
            );
        }

        subscriptionRepository.changeNotificationTypeByChannelIdAndProfileId(
                dto.getType(),dto.getChannelId(),profile.getId());
    }

    private SubscriptionEntity toEntity(SubscriptionCreateDTO dto,Integer profileId) {
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.setChannelId(dto.getChannelId());
        entity.setProfileId(profileId);
        entity.setType(dto.getType());

        return entity;
    }

    public List<SubscriptionInfoDTO> listByProfileId(Integer profileId) {
        if (!profileService.exists(profileId)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<SubscriptionInfo> list = subscriptionRepository.
                listByProfileIdForAdminWithSubscriptionInfo(
                profileId);

        if (list.size() == 0) {
            log.warn("This profile haven't subscribed to any channel");
            throw new BadRequestException(
                    "This profile haven't subscribed to any channel"
            );
        }

        return toResponseListWithSubscriptionInfo(list);
    }

    private List<SubscriptionInfoDTO> toResponseListWithSubscriptionInfo(List<SubscriptionInfo> list) {
        List<SubscriptionInfoDTO> dtoList = new LinkedList<>();

        list.forEach(info -> {
            SubscriptionInfoDTO dto = new SubscriptionInfoDTO();

            ChannelDTO channel = new ChannelDTO();
            channel.setId(info.getChannelId());
            channel.setName(info.getChannelName());
            channel.setPhotoId(info.getChannelPhotoId());
            channel.setPhotoUrl(attachService.getUrl(channel.getPhotoId()));

            SubscriptionDTO subscription = new SubscriptionDTO();
            subscription.setId(info.getSubscriptionId());
            subscription.setType(info.getNotificationType());
            subscription.setChannel(channel);
            subscription.setCreatedDate(info.getCreatedDate());

            dto.setSubscription(subscription);

            dtoList.add(dto);
        });

        return dtoList;

    }
}
