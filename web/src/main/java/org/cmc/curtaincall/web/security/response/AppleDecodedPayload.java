package org.cmc.curtaincall.web.security.response;

import lombok.Builder;

@Builder
public record AppleDecodedPayload(
        String iss,
        String aud,
        String sub,
        String email
) {
}
