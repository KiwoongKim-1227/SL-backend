package com.gdg.slbackend.service.community;

import com.gdg.slbackend.api.community.dto.CommunityMembershipRequest;
import com.gdg.slbackend.api.community.dto.CommunityMembershipResponse;
import com.gdg.slbackend.api.community.dto.CommunityRequest;
import com.gdg.slbackend.api.community.dto.CommunityResponse;
import com.gdg.slbackend.domain.community.Community;
import com.gdg.slbackend.domain.community.CommunityMembership;
import com.gdg.slbackend.domain.user.User;
import com.gdg.slbackend.global.enums.Role;
import com.gdg.slbackend.global.exception.ErrorCode;
import com.gdg.slbackend.global.exception.GlobalException;
import com.gdg.slbackend.global.security.UserPrincipal;
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

    public CommunityResponse createCommunity(CommunityRequest communityRequest, UserPrincipal principal) {
        User user = userFinder.findByIdOrThrow(principal.getId());
        Community community = communityCreator.create(communityRequest, user);

        communityMembershipCreator.createCommunityMembership(
                new CommunityMembershipRequest(community.getId()),
                user.getId(),
                Role.ADMIN,
                true
        );

        return CommunityResponse.from(community);
    }

    @Transactional(readOnly = true)
public CommunityResponse getCommunity(Long communityId, UserPrincipal principal) {
    Community community = communityFinder.findByIdOrThrow(communityId);

    Optional<CommunityMembership> membershipOpt =
            communityMembershipFinder.findById(
                    communityId,
                    principal.getId()
            );

    CommunityMembershipResponse membershipResponse =
            membershipOpt
                    .map(m -> CommunityMembershipResponse.from(
                            m,
                            m.getUser(),
                            community
                    ))
                    .orElse(null);

    return CommunityResponse.from(community, membershipResponse);
}

    @Transactional(readOnly = true)
public List<CommunityResponse> getCommunityAll(UserPrincipal principal) {

    List<Community> communities = communityFinder.findAll();

    List<CommunityMembership> memberships =
            communityMembershipFinder.findAllByUserId(principal.getId());

    Map<Long, CommunityMembership> membershipMap =
            memberships.stream()
                    .collect(Collectors.toMap(
                            m -> m.getCommunity().getId(),
                            m -> m
                    ));

    return communities.stream()
            .sorted((c1, c2) -> {
                boolean p1 = membershipMap.get(c1.getId()) != null
                        && membershipMap.get(c1.getId()).isPinned();
                boolean p2 = membershipMap.get(c2.getId()) != null
                        && membershipMap.get(c2.getId()).isPinned();
                return Boolean.compare(p2, p1);
            })
            .map(c -> {
                CommunityMembership m = membershipMap.get(c.getId());
                CommunityMembershipResponse mr =
                        m == null ? null
                                : CommunityMembershipResponse.from(
                                        m,
                                        m.getUser(),
                                        c
                                );
                return CommunityResponse.from(c, mr);
            })
            .toList();
}



    @Transactional
    public CommunityResponse updateCommunityAdmin(Long communityId, UserPrincipal principal, Long newAdminUserId) {
        /* 해당 커뮤니티의 관리자가 아닌 경우 */
        if(!communityMembershipFinder.isAdmin(communityId, principal.getId())) {
            throw new GlobalException(ErrorCode.COMMUNITY_NOT_ADMIN);
        }
        /* 시스템 관리자가 아닌 경우 */
        if(!userFinder.isSystemAdmin(principal.getId())) {
            throw new GlobalException(ErrorCode.USER_NOT_SYSTEM_ADMIN);
        }

        return CommunityResponse.from(communityUpdater.updateCommunityAdmin(communityId, newAdminUserId));
    }

    @Transactional
    public CommunityResponse updateCommunityPinned(Long communityId, UserPrincipal principal) {
        Community community = communityFinder.findByIdOrThrow(communityId);

        Optional<CommunityMembership> communityMembershipOpt = communityMembershipFinder.findById(communityId, principal.getId());

        CommunityMembership membership = communityMembershipOpt.orElseGet(() ->
                communityMembershipCreator.createCommunityMembershipByCommunityId(
                        communityId,
                        principal.getId(),
                        Role.MEMBER,
                        true
                )
        );

        if (communityMembershipOpt.isPresent()) {
            communityMembershipUpdater.updatePinned(membership);
        }

        CommunityMembershipResponse membershipResponse = CommunityMembershipResponse.from(
                membership,
                membership.getUser(),
                community
        );

        return CommunityResponse.from(community, membershipResponse);
    }


    public void deleteCommunity(Long communityId, UserPrincipal principal) {
        if (communityMembershipFinder.isAdmin(principal.getId(), communityId)) {
            communityDeleter.deleteById(communityId);
        }
        else {
            throw new GlobalException(ErrorCode.COMMUNITY_NOT_ADMIN);
        }
    }
}
