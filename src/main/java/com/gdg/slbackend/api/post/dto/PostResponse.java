package com.gdg.slbackend.api.post.dto;

import com.gdg.slbackend.domain.post.Post;
import com.gdg.slbackend.global.enums.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Long views;
    private long likes;
    private boolean likedByMe;
    private Category category;
    private boolean pinned;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long authorId;
    private String authorNickname;
    private long comments;
    private Long communityId;

    public static PostResponse from(Post post, long comments, boolean likedByMe) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .views(post.getViews())
                .likes(post.getLikes())
                .likedByMe(likedByMe)
                .pinned(post.isPinned())
                .authorId(post.getAuthorId())
                .authorNickname(post.getAuthorNickname())
                .communityId(post.getCommunityId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .category(post.getCategory())
                .comments(comments)
                .build();
    }

    public static PostResponse from(Post post) {
        return from(post, 0, false);
    }

    public static PostResponse from(Post post, long l) {
        return from(post, l, false);
    }
}
