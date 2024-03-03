package org.cmc.curtaincall.web.security.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginResponse(
        Long memberId,
        String accessToken,
        LocalDateTime accessTokenExpiresAt,
        String refreshToken,
        LocalDateTime refreshTokenExpiresAt
) {

}
