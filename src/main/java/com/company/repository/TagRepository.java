package com.company.repository;

import com.company.entity.TagEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {
    @Modifying
    @Transactional
    @Query("update TagEntity set name=:name where id=:id")
    void update(@Param("name") String name,
                @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update TagEntity set visible=:visible where id=:id")
    void updateVisible(@Param("visible") Boolean visible,
                       @Param("id") Integer id);

    boolean existsByIdAndVisible(Integer id, Boolean visible);

    List<TagEntity> findAllByVisible(Boolean aTrue);

    boolean existsByNameAndVisible(String tagName, Boolean visible);

    Optional<TagEntity> findByNameAndVisible(String tagName, Boolean visible);


    @Query("select new TagEntity(t.id,t.name) from TagEntity t " +
            " inner join VideoTagEntity vt " +
            " on t.id=vt.tagId " +
            " inner join VideoEntity v " +
            " on v.id = vt.videoId " +
            " where v.id=:id")
    List<TagEntity> getTagListByVideoId(@Param("id") String videoId);
}
