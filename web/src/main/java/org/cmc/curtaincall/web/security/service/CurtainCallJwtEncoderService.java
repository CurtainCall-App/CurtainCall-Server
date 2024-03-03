package org.cmc.curtaincall.web.security.service;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;

@RequiredArgsConstructor
public class CurtainCallJwtEncoderService {

    private final AccountRepository accountRepository;

    private final JwtEncoder jwtEncoder;

    private final JwtEncoder refreshJwtEncoder;

    private final long accessTokenValidityInMillis;

    private final long refreshTokenValidityInMillis;

    public Jwt encode(String username) {
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(accessTokenValidityInMillis);
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        return jwtEncoder.encode(jwtEncoderParameters);
    }
}
