package org.cmc.curtaincall.web.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2Login {

    @NotBlank
    private String accessToken;
}
