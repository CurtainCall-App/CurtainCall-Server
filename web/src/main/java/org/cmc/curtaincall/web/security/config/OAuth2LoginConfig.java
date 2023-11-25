package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.cmc.curtaincall.web.security.service.UsernameService;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2LoginConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Profile("local")
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
    @Profile("local")
    public OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler(
            final ObjectMapper objectMapper,
            final CurtainCallJwtEncoderService jwtEncoderService,
            final UsernameService usernameService,
            final AccountDao accountDao) {
        return new OAuth2LoginAuthenticationSuccessHandler(
                objectMapper, jwtEncoderService, usernameService, accountDao);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain accessTokenSecurityFilterChain(
            HttpSecurity http,
            JwtIssuerAuthenticationManagerResolver authenticationManagerResolver
    ) throws Exception {
        return http.securityMatcher("/v1/token", "/signup")
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2Login(oauth2Login -> oauth2Login.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .authenticationManagerResolver(authenticationManagerResolver)
                )
                .build();
    }

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver(
            OAuth2ClientProperties properties) {
        final List<String> trustedIssuers = properties.getProvider()
                .values().stream()
                .map(OAuth2ClientProperties.Provider::getIssuerUri)
                .toList();
        return new JwtIssuerAuthenticationManagerResolver(trustedIssuers);
    }

}
