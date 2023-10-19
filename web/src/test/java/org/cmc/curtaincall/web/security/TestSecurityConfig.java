package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.security.jwt.BearerTokenResolver;
import org.cmc.curtaincall.web.security.jwt.JwtAuthenticationCheckFilter;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    public static final String TEST_USERNAME = "test-user";

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter
    ) throws Exception {
        return httpSecurity
                .csrf(config -> config.disable())
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable())
                .oauth2Login(config -> config.disable())
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
                        .anyRequest().authenticated()
                )
                .addFilterAt(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(final JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider, new BearerTokenResolver());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        String token = "ACCESS_TOKEN";
        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getSubject(token)).willReturn(TEST_USERNAME);
        return jwtTokenProvider;
    }
}
