package com.company.mapper;

import com.company.enums.EntityType;

public interface ReportInfo {
    Integer getReportId();
    String getReportContent();
    String getEntityId();
    EntityType getReportType();

    Integer getProfileId();
    String getProfileUsername();
    String getProfilePhotoId();





//    RepostInfo
//    id,profile(id,name,surname,photo(id,key,url)),content,
//    entity_id(channel/video)),type(channel,video)
}
