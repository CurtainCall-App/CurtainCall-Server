package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] PERMITTED_GET_PATH = {
            "/code",
            "/facilities/{facilityId}",
            "/shows",
            "/search/shows",
            "/shows-to-open",
            "/shows-to-end",
            "/shows/{showId}",
            "/box-office",
            "/shows/{showId}/reviews",
            "/notices",
            "/notices/{noticeId}",
            "/members/duplicate/nickname",
            "/show-recommendations",
            "/cost-effective-shows",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity httpSecurity,
            final JwtDecoder curtainCallJwtDecoder,
            final CurtainCallRefreshTokenAuthenticationFilter curtainCallRefreshTokenAuthenticationFilter
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
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAt(curtainCallRefreshTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(config -> config
                        .requestMatchers(HttpMethod.GET,
                                PERMITTED_GET_PATH
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/docs/**");
    }

    @Bean
    public CurtainCallRefreshTokenAuthenticationFilter curtainCallRefreshTokenAuthenticationFilter(
            final JwtDecoder curtainCallJwtRefreshTokenDecoder,
            final ObjectMapper objectMapper,
            final CurtainCallLoginAuthenticationSuccessHandler authenticationSuccessHandler
    ) {
        final CurtainCallRefreshTokenAuthenticationFilter authenticationFilter = new CurtainCallRefreshTokenAuthenticationFilter(
                curtainCallJwtRefreshTokenDecoder,
                objectMapper
        );
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return authenticationFilter;
    }
}
