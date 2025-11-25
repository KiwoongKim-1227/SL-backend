package com.gdg.slbackend.domain.community;

import com.gdg.slbackend.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "communities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends BaseTimeEntity {
    /**
     * This is Entity the information of communities.
     * Each Communities Authority Managed by CommunityMembership.
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int year;

    private int semester;

    @JoinColumn(name = "created_by")
    private Long createdByUserId;

    @Builder
    protected Community(String name, int year, int semester, LocalDate createdAt, LocalDate updateAt, Long createdByUserId) {
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.createdByUserId = createdByUserId;
    }

    public void updateCreatedByUserId(Long userId) {
        this.createdByUserId = userId;
    }
}
