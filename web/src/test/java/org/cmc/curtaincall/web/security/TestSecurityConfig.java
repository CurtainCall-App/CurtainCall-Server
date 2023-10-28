package org.cmc.curtaincall.web.security;

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
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    public static final String TEST_USERNAME = "test-user";

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
                        .requestMatchers(HttpMethod.GET,
                                Stream.of(
                                                SecurityConfig.PERMITTED_GET_PATH,
                                                new String[] {
                                                        "/oauth2/authorization/{provider}"
                                                }
                                        )
                                        .flatMap(Stream::of)
                                        .toArray(String[]::new)
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/login/oauth2/code/{provider}"
                        ).permitAll()
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
        String token = "ACCESS_TOKEN";
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(1000 * 60 * 60);
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", MacAlgorithm.HS256.getName())
                .subject(TEST_USERNAME)
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        given(jwtDecoder.decode(token)).willReturn(jwt);
        return jwtDecoder;
    }
}
