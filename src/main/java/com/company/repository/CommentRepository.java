package com.company.repository;

import com.company.entity.CommentEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends PagingAndSortingRepository<CommentEntity,Integer> {
    Optional<CommentEntity> findByIdAndVisible(Integer id, Boolean visible);

    @Modifying
    @Transactional
    @Query("update CommentEntity set content=?1,updatedDate = ?2  where id=?3")
    void update(String content, LocalDateTime updatedDate, Integer id);

    boolean existsByIdAndVisible(Integer id, Boolean visible);

    @Modifying
    @Transactional
    @Query("update CommentEntity set visible=?1 where id=?2")
    void changeVisible(Boolean visible, Integer id);

    List<CommentEntity> findAllByProfileId(Integer id);

    @Query(value = " select c.id as commentId,c.content as content,c.created_date as createdDate, " +
            " p.id as profileId,p.username as username,p.photo_id as photoId" +
            " from comment c " +
            " inner join profile p " +
            " on c.profile_id=p.id " +
            " where c.video_id=?1 " +
            " and c.visible=true " +
            " order by c.id,c.content,c.created_date, " +
            " p.id,p.username,p.photo_id ",nativeQuery = true)
    List<CommentInfoRepository> findAllByVideoIdWithInfo(String id);

    @Query(value = " select c.id as commentId,c.content as content,c.created_date as createdDate, " +
            " p.id as profileId,p.username as username,p.photo_id as photoId" +
            " from comment c " +
            " inner join profile p " +
            " on c.profile_id=p.id " +
            " where c.id=?1 " +
            " and c.visible=true " +
            " order by c.id,c.content,c.created_date, " +
            " p.id,p.username,p.photo_id ",nativeQuery = true)
    List<CommentInfoRepository> findAllByCommentIdWithInfo(Integer id);
}
