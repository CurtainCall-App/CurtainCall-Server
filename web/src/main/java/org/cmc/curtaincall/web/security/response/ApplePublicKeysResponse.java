package org.cmc.curtaincall.web.security.response;

import java.util.List;

public record ApplePublicKeysResponse(
        List<ApplePublicKeyResponse> keys
) {
}
