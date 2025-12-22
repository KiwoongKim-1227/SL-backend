package com.gdg.slbackend.service.community;

import com.gdg.slbackend.api.community.dto.CommunityMembershipRequest;
import com.gdg.slbackend.api.community.dto.CommunityRequest;
import com.gdg.slbackend.api.community.dto.CommunityResponse;
import com.gdg.slbackend.domain.community.Community;
import com.gdg.slbackend.domain.community.CommunityMembership;
import com.gdg.slbackend.domain.user.User;
import com.gdg.slbackend.global.enums.Role;
import com.gdg.slbackend.global.exception.ErrorCode;
import com.gdg.slbackend.global.exception.GlobalException;
import com.gdg.slbackend.service.communityMembership.CommunityMembershipCreator;
import com.gdg.slbackend.service.communityMembership.CommunityMembershipFinder;
import com.gdg.slbackend.service.communityMembership.CommunityMembershipUpdater;
import com.gdg.slbackend.service.user.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityCreator communityCreator;
    private final CommunityFinder communityFinder;
    private final CommunityUpdater communityUpdater;
    private final CommunityDeleter communityDeleter;
    private final UserFinder userFinder;
    private final CommunityMembershipCreator communityMembershipCreator;
    private final CommunityMembershipFinder communityMembershipFinder;
    private final CommunityMembershipUpdater communityMembershipUpdater;

    public CommunityResponse createCommunity(CommunityRequest communityRequest, Long userId) {
        User user = userFinder.findByIdOrThrow(userId);
        Community community = communityCreator.create(communityRequest, user);

        communityMembershipCreator.createCommunityMembership(
                new CommunityMembershipRequest(community.getId()),
                userId,
                Role.ADMIN,
                true
        );

        return CommunityResponse.from(community);
    }

    @Transactional(readOnly = true)
    public CommunityResponse getCommunity(Long communityId, Long userId) {
        Community community = communityFinder.findByIdOrThrow(communityId);

        Optional<CommunityMembership> communityMembership = communityMembershipFinder.findById(communityId, userId);

        return CommunityResponse.from(community, communityMembership.orElse(null));
    }

    @Transactional(readOnly = true)
    public List<CommunityResponse> getCommunityAll(Long userId) {
        List<Community> communities = communityFinder.findAll();

        List<CommunityMembership> memberships = communityMembershipFinder.findAllByUserId(userId);

        Map<Long, CommunityMembership> membershipMap = memberships.stream()
                .collect(Collectors.toMap(
                        m -> m.getCommunity().getId(),
                        m -> m
                ));

        return communities.stream()
                .sorted((c1, c2) -> Boolean.compare(
                        membershipMap.getOrDefault(c2.getId(), null) != null && membershipMap.get(c2.getId()).isPinned(),
                        membershipMap.getOrDefault(c1.getId(), null) != null && membershipMap.get(c1.getId()).isPinned()
                ))
                .map(c -> CommunityResponse.from(c, membershipMap.getOrDefault(c.getId(), null)))
                .toList();
    }


    @Transactional
    public CommunityResponse updateCommunityAdmin(Long communityId, Long newAdminUserId, Long requestUserId) {
        /* 해당 커뮤니티의 관리자가 아닌 경우 */
        if(!communityMembershipFinder.isAdmin(communityId, requestUserId)) {
            throw new GlobalException(ErrorCode.COMMUNITY_NOT_ADMIN);
        }
        /* 시스템 관리자가 아닌 경우 */
        if(!userFinder.isSystemAdmin(requestUserId)) {
            throw new GlobalException(ErrorCode.USER_NOT_SYSTEM_ADMIN);
        }

        return CommunityResponse.from(communityUpdater.updateCommunityAdmin(communityId, newAdminUserId));
    }

    @Transactional
    public CommunityResponse updateCommunityPinned(Long communityId, Long userId) {
        Community community = communityFinder.findByIdOrThrow(communityId);

        Optional<CommunityMembership> communityMembership = communityMembershipFinder.findById(communityId, userId);

        if (communityMembership.isEmpty()) {
            CommunityMembership newMembership = communityMembershipCreator.createCommunityMembershipByCommunityId(
                    communityId,
                    userId,
                    Role.MEMBER,
                    true
            );
            communityMembership = Optional.of(newMembership);
        } else {
            communityMembershipUpdater.updatePinned(communityMembership.get());
        }

        return CommunityResponse.from(community, communityMembership.orElse(null));
    }

    public void deleteCommunity(Long communityId, Long userId) {
        if (communityMembershipFinder.isAdmin(userId, communityId)) {
            communityDeleter.deleteById(communityId);
        }
        else {
            throw new GlobalException(ErrorCode.COMMUNITY_NOT_ADMIN);
        }
    }
}
