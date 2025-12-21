package com.gdg.slbackend.api.resource.dto;

import com.gdg.slbackend.domain.resource.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceDownloadResponse {
    private String downloadUrl;

    public static ResourceDownloadResponse from(Resource resource) {
        return new ResourceDownloadResponse(
                resource.getImageUrl()
        );
    }
}
