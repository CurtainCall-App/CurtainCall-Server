package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cmc.curtaincall.web.security.request.TokenLoginRequest;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class CurtainCallRefreshTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String FILTER_PROCESSES_URI = "/login/refresh";

    private final ObjectMapper objectMapper;

    public CurtainCallRefreshTokenAuthenticationFilter(
            final JwtDecoder jwtDecoder,
            final ObjectMapper objectMapper
    ) {
        super(FILTER_PROCESSES_URI);
        setAuthenticationManager(new ProviderManager(
                new JwtAuthenticationProvider(jwtDecoder)
        ));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        final TokenLoginRequest loginRequest = objectMapper.readValue(
                request.getInputStream(), TokenLoginRequest.class);
        return getAuthenticationManager().authenticate(
                new BearerTokenAuthenticationToken(loginRequest.token())
        );
    }

}
