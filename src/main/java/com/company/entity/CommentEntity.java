package com.company.entity;

import com.company.entity.video.VideoEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "profile_id")
    private Integer profileId;

    @JoinColumn(nullable = false, name = "profile_id",insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(name = "comment_id")
    private Integer commentId;

    @JoinColumn(name = "comment_id",nullable = false,insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity comment;

    @Column(name = "video_id")
    private String videoId;

    @JoinColumn(name = "video_id",nullable = false,insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private VideoEntity video;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

    public CommentEntity(Integer id,String content,LocalDateTime createdDate) {
        this.id = id;
        this.content=content;
        this.createdDate = createdDate;
    }
}
