package org.cmc.curtaincall.web.security.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain accessTokenSecurityFilterChain(
            HttpSecurity http,
            JwtIssuerAuthenticationManagerResolver authenticationManagerResolver
    ) throws Exception {
        return http.securityMatcher("/login", "/signup")
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
