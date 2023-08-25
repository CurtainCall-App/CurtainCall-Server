package org.cmc.curtaincall.web.security.response;

public record ApplePublicKeyResponse(
        String kid,
        String alg,
        String use,
        String n,
        String e
) {
}
