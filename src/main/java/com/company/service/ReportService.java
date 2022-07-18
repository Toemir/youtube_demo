package com.company.service;

import com.company.dto.ReportCreateDTO;
import com.company.dto.ReportDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.ReportEntity;
import com.company.enums.EntityType;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.exceptions.ProfileNotFoundException;
import com.company.mapper.ReportInfo;
import com.company.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    private final ChannelService channelService;

    private final VideoService videoService;

    private final AttachService attachService;

    private final ProfileService profileService;

    public ReportService(ReportRepository reportRepository, ChannelService channelService, VideoService videoService, AttachService attachService, ProfileService profileService) {
        this.reportRepository = reportRepository;
        this.channelService = channelService;
        this.videoService = videoService;
        this.attachService = attachService;
        this.profileService = profileService;
    }

    public void create(ReportCreateDTO dto) {
        if (dto.getType().equals(EntityType.CHANNEL)) {

            if (!channelService.exists(dto.getEntityId())) {
                log.error("Channel not found");
                throw new ItemNotFoundException(
                        "Channel not found"
                );
            }

        } else {

            if (!videoService.exists(dto.getEntityId())) {
                log.error("Video not found");
                throw new ItemNotFoundException(
                        "Video not found"
                );
            }

        }

        if (reportRepository.existsByProfileIdAndEntityId(dto.getProfileId(),dto.getEntityId())) {
            log.error("Already reported by this profile");
            throw new ItemAlreadyExistsException(
                    "Already reported by this profile"
            );
        }

        ReportEntity entity = toEntity(dto);

        reportRepository.save(entity);
    }

    private ReportEntity toEntity(ReportCreateDTO dto) {
        ReportEntity entity = new ReportEntity();
        entity.setEntityId(dto.getEntityId());
        entity.setProfileId(dto.getProfileId());
        entity.setContent(dto.getContent());
        entity.setType(dto.getType());

        return entity;
    }

    public List<ReportDTO> pagination(int size, int page) {
        List<ReportInfo> list = reportRepository.paginationWithReportInfo(size, page);

        if (list.size() == 0) {
            log.warn("No reports found yet");
            throw new ItemNotFoundException(
                    "No reports found yet"
            );
        }

        return toResponseList(list);
    }

    public List<ReportDTO> list(Integer profileId) {
        if (!profileService.exists(profileId)) {
            log.error("Profile not found!");
            throw new ProfileNotFoundException(
                    "Profile not found!"
            );
        }

        List<ReportInfo> list = reportRepository.findAllByProfileIdWithReportInfo(profileId);

        if (list.size() == 0) {
            log.warn("No reports found by this profile");
            throw new ItemNotFoundException(
                    "No reports found by this profile"
            );
        }

        return toResponseList(list);
    }

    private List<ReportDTO> toResponseList(List<ReportInfo> list) {
        List<ReportDTO> dtoList = new LinkedList<>();

        list.forEach(info -> {
            ReportDTO dto = new ReportDTO();
            dto.setId(info.getReportId());
            dto.setContent(info.getReportContent());
            dto.setEntityId(info.getEntityId());
            dto.setType(info.getReportType());

            ProfileDTO profile = new ProfileDTO();
            profile.setId(info.getProfileId());
            profile.setUsername(info.getProfileUsername());
            profile.setPhotoId(info.getProfilePhotoId());
            profile.setPhotoUrl(attachService.getUrl(profile.getPhotoId()));

            dto.setProfile(profile);

            dtoList.add(dto);
        });

        return dtoList;
    }
}
