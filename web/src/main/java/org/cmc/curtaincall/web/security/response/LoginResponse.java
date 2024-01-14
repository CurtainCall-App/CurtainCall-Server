package org.cmc.curtaincall.web.security.response;

import java.time.LocalDateTime;

public record LoginResponse(
        Long memberId,
        String accessToken,
        LocalDateTime accessTokenExpiresAt
) {

}
