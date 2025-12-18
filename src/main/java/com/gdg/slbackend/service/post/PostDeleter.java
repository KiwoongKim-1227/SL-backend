package com.gdg.slbackend.service.post;

import com.gdg.slbackend.domain.post.Post;
import com.gdg.slbackend.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDeleter {

    private final PostRepository postRepository;

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }
}
