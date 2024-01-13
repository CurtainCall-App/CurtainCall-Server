package org.cmc.curtaincall.web.security.config.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.cmc.curtaincall.web.security.service.UsernameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Profile("local")
@Configuration
public class LocalSecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauth2LoginSecurityFilterChain(
            HttpSecurity http,
            OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler
    ) throws Exception {
        return http.securityMatcher("/oauth2/**", "/login/oauth2/**")
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oAuth2LoginAuthenticationSuccessHandler)
                )
                .build();
    }

    @Bean
    public OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler(
            final ObjectMapper objectMapper,
            final CurtainCallJwtEncoderService jwtEncoderService,
            final UsernameService usernameService,
            final AccountDao accountDao) {
        return new OAuth2LoginAuthenticationSuccessHandler(
                objectMapper, jwtEncoderService, usernameService, accountDao);
    }
}
