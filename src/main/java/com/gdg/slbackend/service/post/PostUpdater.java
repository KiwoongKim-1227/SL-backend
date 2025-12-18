package com.gdg.slbackend.service.post;

import com.gdg.slbackend.api.post.dto.PostRequest;
import com.gdg.slbackend.domain.post.Post;
import com.gdg.slbackend.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostUpdater {
    private final PostFinder postFinder;

    public void updatePinned(Post post) {
        if (!post.isPinned()) {
            unpinExistingPost(post.getCommunityId());
        }

        post.togglePinned();
    }

    private void unpinExistingPost(Long communityId) {
        postFinder.findPinnedPost(communityId)
                .ifPresent(Post::togglePinned);
    }

    public void updatePost(PostRequest postRequest, Post post) {
        if (postRequest.getTitle() != null) {
            post.updateTitle(postRequest.getTitle());
        }

        if (postRequest.getContent() != null) {
            post.updateContent(postRequest.getContent());
        }

        if (postRequest.getCategory() != null) {
            post.updateCategory(postRequest.getCategory());
        }
    }

    public void updateViews(Post post) {
        post.increaseViews();
    }

    public void updateLikes(Post post) {
        post.increaseLikes();
    }
}
