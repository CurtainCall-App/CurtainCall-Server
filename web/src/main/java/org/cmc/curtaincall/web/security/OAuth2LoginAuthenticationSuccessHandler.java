package org.cmc.curtaincall.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class OAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final CurtainCallJwtEncoderService jwtEncoderService;

    private final UsernameService usernameService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {
        String username = usernameService.getUsername(authentication);
        Jwt jwt = jwtEncoderService.encode(username);
        LoginResponse loginResponse = new LoginResponse(jwt.getTokenValue());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), loginResponse);
    }
}