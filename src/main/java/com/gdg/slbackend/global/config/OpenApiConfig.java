package com.gdg.slbackend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SL Backend API",
                version = "v1",
                description = "<h1>익명 메모 기능을 포함한 SKHU Link 백엔드 API</h1>" +
                        "</hr>" +
                        "<h3>사용 방법</h3>" +
                        "<ul>\n" +
                        "<li>1. https://skhu-link.duckdns.org/ 주소로 로그인 하여 토큰을 발급 받는다.</li>\n" +
                        "<li>2. accessToken을 복사한다.</li>\n" +
                        "<li>3. Swagger-ui에서 Servers 중 배포 환경으로 선택한다.</li>\n" +
                        "<li>4. Authorize를 눌러 accessToken을 붙여 넣고 Authorize를 누른다.</li>\n" +
                        "<li>5. API 요청을 수행한다.</li>\n" +
                        "<li>undefined. 만약 토큰이 만료되었다면 refreshToken으로 /auth/refresh 에 요청하여 accessToken을 재발급 받는다.</li>\n" +
                        "</ul>",
                contact = @Contact(name = "GDG",
                        email = "support@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 환경"),
                @Server(url = "https://skhu-link.duckdns.org", description = "배포 환경")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}