package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class OAuth2LoginConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain accessTokenSecurityFilterChain(
            final HttpSecurity http,
            final OAuth2TokenLoginAuthenticationFilter authenticationFilter
    ) throws Exception {
        return http.securityMatcher("/login/oauth2/token/*")
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2Login(oauth2Login -> oauth2Login.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.disable())
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public OAuth2TokenLoginAuthenticationFilter oAuth2TokenLoginAuthenticationFilter(
            final JwtIssuerAuthenticationManagerResolver authenticationManagerResolver,
            final ClientRegistrationRepository clientRegistrationRepository,
            final CurtainCallLoginAuthenticationSuccessHandler authenticationSuccessHandler,
            final OAuth2ClientProperties properties,
            final ObjectMapper objectMapper
    ) {

        final Map<String, String> issuerUriToProviderName = properties.getProvider().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri() != null)
                .collect(Collectors.toMap(entry -> entry.getValue().getIssuerUri(), Map.Entry::getKey));
        final OAuth2TokenLoginAuthenticationFilter authenticationFilter = new OAuth2TokenLoginAuthenticationFilter(
                authenticationManagerResolver,
                clientRegistrationRepository,
                issuerUriToProviderName,
                objectMapper
        );
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return authenticationFilter;
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
