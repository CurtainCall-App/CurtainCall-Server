package org.cmc.curtaincall.web.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final SecretKey secretKey;

    private final long accessTokenValidityInMillis;

    private final long refreshTokenValidityInMillis;

    private final JwtParser jwtParser;

    public JwtTokenProvider(
            long accessTokenValidity,
            long refreshTokenValidity,
            String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessTokenValidityInMillis = accessTokenValidity * 1000;
        this.refreshTokenValidityInMillis = refreshTokenValidity * 1000;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String createAccessToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + accessTokenValidityInMillis);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + refreshTokenValidityInMillis);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: ", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: ", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: ", e);
        }
        return false;
    }

    public String getSubject(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpiration(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getExpiration();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }

    public long getRefreshTokenValidityInDay() {
        return refreshTokenValidityInMillis / (24 * 60 * 60 * 1000);
    }
}
