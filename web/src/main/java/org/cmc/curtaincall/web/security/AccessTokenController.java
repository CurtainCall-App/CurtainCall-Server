package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
public class AccessTokenController {

    private final UsernameService usernameService;

    private final CurtainCallJwtEncoderService jwtEncoderService;

    private final AccountDao accountDao;

    @PostMapping("/v1/token")
    public LoginResponse issueAccessToken(Authentication authentication) {
        String username = usernameService.getUsername(authentication);
        Jwt jwt = jwtEncoderService.encode(username);
        final MemberId memberId = accountDao.getMemberId(username);
        final LocalDateTime expiresAt = LocalDateTime.ofInstant(jwt.getExpiresAt(), ZoneId.systemDefault());
        return new LoginResponse(memberId.getId(), jwt.getTokenValue(), expiresAt);
    }
}
