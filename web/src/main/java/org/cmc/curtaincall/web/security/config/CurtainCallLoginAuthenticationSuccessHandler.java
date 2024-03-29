package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class CurtainCallLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final CurtainCallJwtEncoderService jwtEncoderService;

    private final AccountRepository accountRepository;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        final String username = authentication.getName();
        final Jwt accessToken = jwtEncoderService.getAccessToken(username);
        final Jwt refreshToken = jwtEncoderService.getRefreshToken(username);
        final LocalDateTime accessTokenExpiresAt = LocalDateTime.ofInstant(
                Objects.requireNonNull(accessToken.getExpiresAt()), ZoneId.systemDefault());
        final LocalDateTime refreshTokenExpiresAt = LocalDateTime.ofInstant(
                Objects.requireNonNull(refreshToken.getExpiresAt()), ZoneId.systemDefault());
        final Account account = accountRepository.findByUsername(username)
                .orElseGet(() -> new Account(username));
        account.renewRefreshToken(
                refreshToken.getTokenValue(),
                refreshTokenExpiresAt
        );
        final LoginResponse loginResponse = new LoginResponse(
                Optional.ofNullable(account.getMemberId()).map(MemberId::getId).orElse(null),
                accessToken.getTokenValue(),
                accessTokenExpiresAt,
                refreshToken.getTokenValue(),
                refreshTokenExpiresAt
        );
        accountRepository.save(account);
        objectMapper.writeValue(response.getWriter(), loginResponse);
    }
}
