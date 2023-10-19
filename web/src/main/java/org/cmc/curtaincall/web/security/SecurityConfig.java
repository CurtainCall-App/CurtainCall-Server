package org.cmc.curtaincall.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.security.jwt.BearerTokenResolver;
import org.cmc.curtaincall.web.security.jwt.JwtAuthenticationCheckFilter;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.cmc.curtaincall.web.security.oauth2.OidcAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
            OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler
    ) throws Exception {
        return httpSecurity
                .csrf(config -> config.disable())
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable())
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(config -> config
                        .requestMatchers(HttpMethod.GET,
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
                                "/notices/{noticeId}"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAt(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oidcAuthenticationSuccessHandler)
                )
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/docs/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public OidcAuthenticationSuccessHandler oidcAuthenticationSuccessHandler(
            final ObjectMapper objectMapper, final JwtTokenProvider jwtTokenProvider) {
        return new OidcAuthenticationSuccessHandler(objectMapper, jwtTokenProvider);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.secret}") String secret) {
        return new JwtTokenProvider(accessTokenValidity, secret);
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider, new BearerTokenResolver());
    }
}
