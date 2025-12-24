package com.gdg.slbackend.domain.resource;

import com.gdg.slbackend.domain.user.User;
import com.gdg.slbackend.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader")
    private User uploader;

    private String title;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * S3 object key (ex: resources/uuid_filename.pdf)
     */
    private String imageUrl;

    @Builder
    public Resource(Long communityId, User uploader, String title, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.communityId = communityId;
        this.uploader = uploader;
        this.title = title;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
