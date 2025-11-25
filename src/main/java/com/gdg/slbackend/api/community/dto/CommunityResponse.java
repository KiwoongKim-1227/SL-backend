package com.gdg.slbackend.api.community.dto;

import com.gdg.slbackend.domain.community.Community;
import lombok.Builder;

@Builder
public class CommunityResponse {
    private Long id;
    private String name;
    private int year;
    private int semester;
    private String adminName;

    public static CommunityResponse from(Community community, String adminName) {
        return CommunityResponse.builder()
                .id(community.getId())
                .name(community.getName())
                .year(community.getYear())
                .semester(community.getSemester())
                .adminName(adminName)
                .build();
    }
}
