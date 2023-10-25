package org.cmc.curtaincall.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.cmc.curtaincall.web.security.oauth2.OidcAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2LoginConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauth2LoginSecurityFilterChain(
            HttpSecurity http,
            OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler
    ) throws Exception {
        return http.securityMatcher("/oauth2/**", "/login/oauth2/**")
                .csrf(config -> config.disable())
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable())
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oidcAuthenticationSuccessHandler)
                ).build();
    }

    @Bean
    public OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler(
            final ObjectMapper objectMapper, final JwtTokenProvider jwtTokenProvider) {
        return new OidcAuthenticationSuccessHandler(objectMapper, jwtTokenProvider);
    }
}
