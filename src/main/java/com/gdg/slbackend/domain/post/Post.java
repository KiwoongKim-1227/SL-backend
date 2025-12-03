package com.gdg.slbackend.domain.post;

import com.gdg.slbackend.global.entity.BaseTimeEntity;
import com.gdg.slbackend.global.enums.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Table(name = "posts")
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long communityId;
    private Long authorId;
    private String authorName;
    private Long thumbnailFileId;

    private String title;
    private String content;

    private Category category;

    private boolean pinned;

    private Long views;

//    필요성에 대한 의논 필요
    private LocalDate deletedAt;

    @Builder
    public Post(Long communityId, Long authorId, String authorName, Long thumbnailFileId, String title, String content, Category category, boolean pinned, Long views, LocalDate deletedAt) {
        this.communityId = communityId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.thumbnailFileId = thumbnailFileId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.pinned = pinned;
        this.views = views;
        this.deletedAt = deletedAt;
    }

    public void updateAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void updateThumbnailFileId(Long thumbnailFileId) {
        this.thumbnailFileId = thumbnailFileId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void togglePinned() {
        this.pinned = !pinned;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void increaseViews() {
        ++views;
    }
}
