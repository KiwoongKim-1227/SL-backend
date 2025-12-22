package com.gdg.slbackend.domain.community;

import com.gdg.slbackend.domain.user.User;
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

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "communities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User admin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    protected Community(String name, int year, int semester, LocalDateTime createdAt, LocalDateTime updatedAt, User admin) {
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.admin = admin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateAdmin(User admin) {
        this.admin = admin;
    }

    public void updateUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
