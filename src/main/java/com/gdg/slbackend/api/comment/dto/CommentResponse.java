package com.gdg.slbackend.api.comment.dto;

import com.gdg.slbackend.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private int likes;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean likedByMe;

    public static CommentResponse from(Comment comment, boolean likedByMe) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .likes(comment.getLikeCount())
                .likedByMe(likedByMe)
                .build();
    }
    public static CommentResponse from(Comment comment) {
        return from(comment, false);
    }
}
