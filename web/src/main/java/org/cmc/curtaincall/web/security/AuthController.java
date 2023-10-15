package org.cmc.curtaincall.web.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.exception.AuthenticationException;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.cmc.curtaincall.web.security.oauth2.AppleService;
import org.cmc.curtaincall.web.security.oauth2.OAuth2UserInfo;
import org.cmc.curtaincall.web.security.request.OAuth2Login;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.cmc.curtaincall.web.security.service.AccountService;
import org.cmc.curtaincall.web.security.response.AccountDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;

    private final AccountService accountService;

    private final AppleService appleService;

    @PostMapping("/login/oauth2/token/{registrationId}")
    public ResponseEntity<LoginResponse> loginOauthToken(
            @PathVariable String registrationId, @Valid @RequestBody OAuth2Login oauthLogin)
            throws JsonProcessingException {
        String providerId = getProviderId(registrationId, oauthLogin.getAccessToken());
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, providerId);
        String username = userInfo.ofUsername();

        String accessToken = jwtTokenProvider.createAccessToken(username);
        LocalDateTime accessTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(accessToken).toInstant(), ZoneId.systemDefault());
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        LocalDateTime refreshTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(refreshToken).toInstant(), ZoneId.systemDefault());

        AccountDto account = accountService.login(username, refreshToken, refreshTokenExpiresAt);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(account.refreshToken())
                .refreshTokenExpiresAt(account.refreshTokenExpiresAt())
                .memberId(account.memberId())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    private String getProviderId(String registrationId, String accessToken) throws JsonProcessingException {
        if ("kakao".equals(registrationId)) {
            WebClient webClient = WebClient.builder()
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .build();
            String tokenInfoJson = webClient.get()
                    .uri(getTokenInfoUrl(registrationId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(tokenInfoJson, Map.class).get("id").toString();
        } else if ("apple".equals(registrationId)) {
            return appleService.userIdFromApple(accessToken);
        } else {
            throw new AuthenticationException("registrationId=" + registrationId + ", accessToken=" + accessToken);
        }
    }

    private String getTokenInfoUrl(String registrationId) {
        if ("kakao".equals(registrationId)) {
            return "https://kapi.kakao.com/v1/user/access_token_info";
        }
        throw new AuthenticationException("지원하지 않는 OAuth2 Provider 입니다. provider = " + registrationId);
    }

    @PostMapping("/login/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            return unauthorizedResponse();
        }

        String username = jwtTokenProvider.getSubject(token);
        AccountDto account = accountService.get(username);
        if (!token.equals(account.refreshToken())) {
            return unauthorizedResponse();
        }

        String accessToken = jwtTokenProvider.createAccessToken(username);
        LocalDateTime accessTokenExpiresAt = LocalDateTime.ofInstant(
                jwtTokenProvider.getExpiration(accessToken).toInstant(), ZoneId.systemDefault());
        long refreshTokenValidityInDay = jwtTokenProvider.getRefreshTokenValidityInDay();
        long loginDayTerm = Duration.between(
                LocalDateTime.now().plusDays(refreshTokenValidityInDay), account.refreshTokenExpiresAt()).toDays();
        if (loginDayTerm > 0) {
            String refreshToken = jwtTokenProvider.createRefreshToken(username);
            LocalDateTime refreshTokenExpiresAt = LocalDateTime.ofInstant(
                    jwtTokenProvider.getExpiration(refreshToken).toInstant(), ZoneId.systemDefault());
            account = accountService.login(username, refreshToken, refreshTokenExpiresAt);
        }

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(account.refreshToken())
                .refreshTokenExpiresAt(account.refreshTokenExpiresAt())
                .memberId(account.memberId())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    private static ResponseEntity<?> unauthorizedResponse() {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status, "인증되지 않은 사용자입니다.");
        return ResponseEntity.status(status.value()).body(problemDetail);
    }

}
