package com.gdg.slbackend.service.resource;

import com.gdg.slbackend.domain.resource.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ResourceUpdater {

    @Transactional
    public void update(Resource resource, String title) {
        resource.updateUpdatedAt(LocalDateTime.now());
        resource.updateTitle(title);
    }
}
