package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.security.config.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.time.Instant;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    public static final String TEST_USERNAME = "test-user";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtDecoder curtainCallJwtDecoder
    ) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2Login(oauth2Login -> oauth2Login.disable())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt
                                .decoder(curtainCallJwtDecoder)
                        )
                )
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(config -> config
                        .requestMatchers(HttpMethod.GET, SecurityConfig.PERMITTED_GET_PATH).permitAll()
                        .requestMatchers(HttpMethod.POST, "/login/oauth2/token/*").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }

    @Bean
    public JwtDecoder curtainCallJwtDecoder() {
        JwtDecoder jwtDecoder = mock(JwtDecoder.class);
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(1000 * 60 * 60);
        Jwt jwt = Jwt.withTokenValue(ACCESS_TOKEN)
                .header("alg", MacAlgorithm.HS256.getName())
                .subject(TEST_USERNAME)
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        given(jwtDecoder.decode(ACCESS_TOKEN)).willReturn(jwt);
        return jwtDecoder;
    }
}
