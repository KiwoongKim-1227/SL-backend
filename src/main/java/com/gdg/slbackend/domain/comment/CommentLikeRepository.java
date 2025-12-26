package com.gdg.slbackend.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByUserIdAndComment(Long userId, Comment comment);

    boolean existsByUserIdAndComment(Long userId, Comment comment);
}
