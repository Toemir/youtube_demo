package com.company.repository;

import com.company.entity.CommentLikeEntity;
import com.company.enums.CommentLikeType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {
    Optional<CommentLikeEntity> findByProfileIdAndCommentId(Integer profileId, Integer commentId);


    @Modifying
    @Transactional
    @Query("update CommentLikeEntity set type=?1 where id=?2 ")
    void updateType(CommentLikeType type, Integer id);

    List<CommentLikeEntity> findAllByProfileIdOrderByCreatedDateDesc(Integer profileId);

    List<CommentLikeEntity> findAllByProfileId(Integer profileId);

    boolean existsByProfileIdAndCommentId(Integer profileId, Integer commentId);

    void deleteByProfileIdAndCommentId(Integer profileId, Integer commentId);
}
