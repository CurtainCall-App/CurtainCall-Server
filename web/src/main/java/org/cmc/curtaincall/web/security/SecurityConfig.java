package org.cmc.curtaincall.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.security.jwt.*;
import org.cmc.curtaincall.web.security.oauth2.OAuth2AuthenticationSuccessHandlerCustom;
import org.cmc.curtaincall.web.security.oauth2.OAuth2UserServiceCustom;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            OAuth2UserServiceCustom oAuth2UserService,
            OAuth2AuthenticationSuccessHandlerCustom oAuth2AuthenticationSuccessHandler,
            JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtLogoutHandler jwtLogoutHandler,
            JwtLogoutSuccessHandler jwtLogoutSuccessHandler
    ) throws Exception {
        return httpSecurity
                .csrf(config -> config.disable())
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable())
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(config -> config
                        .requestMatchers(HttpMethod.POST,
                                "/login/oauth2/token/{registrationId}",
                                "/login/reissue"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .logout(logout -> logout
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(config -> config
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
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
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
            @Value("${jwt.secret}") String secret) {
        return new JwtTokenProvider(
                accessTokenValidity, refreshTokenValidity, secret);
    }

    @Bean
    public JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter(
            JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider);
    }

    @Bean
    public JwtLogoutHandler jwtLogoutHandler(JwtTokenProvider jwtTokenProvider, AccountService accountService) {
        return new JwtLogoutHandler(jwtTokenProvider, accountService);
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler(ObjectMapper objectMapper) {
        return new JwtLogoutSuccessHandler();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }
}
