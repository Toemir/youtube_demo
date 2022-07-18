package com.company.repository;

import com.company.entity.ReportEntity;
import com.company.mapper.ReportInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends PagingAndSortingRepository<ReportEntity,Integer> {
    boolean existsByProfileIdAndEntityId(Integer profileId, String entityId);


    @Query(value = "select r.id as reportId,r.content as reportContent," +
            " r.entity_id as reportEntityId,r.type as reportType," +
            " p.id as profileId,p.username as profileUsername," +
            " p.photo_id as profilePhotoId " +
            " from report r " +
            " inner join profile p " +
            " on r.profile_id = p.id " +
            " limit :limit " +
            " offset :offset",nativeQuery = true)
    List<ReportInfo> paginationWithReportInfo(@Param("limit") int size,
                                              @Param("offset") int page);

    @Query(value = "select r.id as reportId,r.content as reportContent," +
            " r.entity_id as reportEntityId,r.type as reportType," +
            " p.id as profileId,p.username as profileUsername," +
            " p.photo_id as profilePhotoId " +
            " from report r " +
            " inner join profile p " +
            " on r.profile_id = p.id " +
            " where p.id = :profileId",nativeQuery = true)
    List<ReportInfo> findAllByProfileIdWithReportInfo(@Param("profileId") Integer profileId);
}
