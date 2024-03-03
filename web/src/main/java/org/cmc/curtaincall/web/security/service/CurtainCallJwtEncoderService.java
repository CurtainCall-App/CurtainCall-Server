package org.cmc.curtaincall.web.security.service;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
public class CurtainCallJwtEncoderService {

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

    public Jwt getAccessToken(final String username) {
        final JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        final Instant issuedAt = Instant.now();
        final Instant expiresAt = issuedAt.plusMillis(accessTokenValidityInMillis);
        final JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("token-type", "access-token")
                .build();
        final JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        return jwtEncoder.encode(jwtEncoderParameters);
    }

    public Jwt getRefreshToken(final String username) {
        final JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
        final Instant issuedAt = Instant.now();
        final Instant expiresAt = issuedAt.plusMillis(refreshTokenValidityInMillis);
        final JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("token-type", "refresh-token")
                .build();
        final JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);
        return refreshJwtEncoder.encode(jwtEncoderParameters);
    }
}
