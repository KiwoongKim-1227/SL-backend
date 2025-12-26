package com.gdg.slbackend.service.resource;

import com.gdg.slbackend.domain.resource.Resource;
import com.gdg.slbackend.domain.resource.ResourceRepository;
import com.gdg.slbackend.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceDeleter {

    private final ResourceRepository resourceRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public void delete(Resource resource) {

        // 1️⃣ S3 key 미리 확보
        String imageUrl = resource.getImageUrl();

        // 2️⃣ DB 먼저 삭제 (핵심)
        resourceRepository.delete(resource);

        // 3️⃣ 파일 삭제는 best-effort (절대 throw 금지)
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                s3Uploader.deleteFile(imageUrl);
            } catch (Exception e) {
                log.error("Failed to delete S3 file. imageUrl={}", imageUrl, e);
            }
        }
    }
}
