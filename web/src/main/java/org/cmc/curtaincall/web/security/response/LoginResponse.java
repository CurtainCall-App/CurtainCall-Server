package org.cmc.curtaincall.web.security.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private Long memberId;

    private String accessToken;

    private LocalDateTime accessTokenExpiresAt;

    private String refreshToken;

    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    private LoginResponse(
            Long memberId,
            String accessToken,
            LocalDateTime accessTokenExpiresAt,
            String refreshToken,
            LocalDateTime refreshTokenExpiresAt) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
