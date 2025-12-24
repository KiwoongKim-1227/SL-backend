package com.gdg.slbackend.global.security;

import com.gdg.slbackend.api.auth.dto.AuthTokenResponse;
import com.gdg.slbackend.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    // ✅ 프론트 배포 주소
    @Value("${app.frontend.prod-callback-url}")
    private String prodCallbackUrl;

    // ✅ 로컬 프론트 고정
    @Value("${app.frontend.local-callback-url}")
    private String localCallbackUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        AuthTokenResponse tokenResponse =
                authService.handleMicrosoftLogin((OAuth2AuthenticationToken) authentication);

        String redirectBaseUrl = resolveRedirectUrl(request);

        String redirectUrl = redirectBaseUrl
                + "#accessToken=" + tokenResponse.getAccessToken()
                + "&refreshToken=" + tokenResponse.getRefreshToken();

        log.info("OAuth2 Login Redirect URL: {}", redirectUrl);
        response.sendRedirect(redirectUrl);

    }

    /**
     * ✅ 배포 백엔드로 접근된 경우만 prod 프론트로 리다이렉트
     * 그 외 모든 경우는 localhost:3000
     */
    private String resolveRedirectUrl(HttpServletRequest request) {
        String serverName = request.getServerName();

        log.info("OAuth2 request serverName: {}", serverName);

        // 배포 백엔드 도메인
        if ("skhu-link.duckdns.org".equalsIgnoreCase(serverName)) {
            return prodCallbackUrl;
        }

        return localCallbackUrl;
    }
}
