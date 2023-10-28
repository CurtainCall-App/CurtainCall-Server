package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccessTokenController {

    private final UsernameService usernameService;

    private final CurtainCallJwtEncoderService jwtEncoderService;

    @PostMapping("/v1/token")
    public LoginResponse issueAccessToken(Authentication authentication) {
        String username = usernameService.getUsername(authentication);
        Jwt jwt = jwtEncoderService.encode(username);
        return new LoginResponse(jwt.getTokenValue());
    }
}
