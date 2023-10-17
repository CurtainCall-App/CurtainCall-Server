package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.security.jwt.JwtAuthenticationCheckFilter;
import org.cmc.curtaincall.web.security.jwt.JwtLogoutHandler;
import org.cmc.curtaincall.web.security.jwt.JwtLogoutSuccessHandler;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.cmc.curtaincall.web.security.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationCheckFilter jwtAuthenticationCheckFilter,
            JwtLogoutHandler jwtLogoutHandler,
            JwtLogoutSuccessHandler jwtLogoutSuccessHandler
    ) throws Exception {
        return httpSecurity
                .csrf(config -> config.disable())
                .formLogin(config -> config.disable())
                .httpBasic(config -> config.disable())
//                .sessionManagement(config -> config
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
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
//                .addFilterAfter(jwtAuthenticationCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
//                .logout(logout -> logout
//                        .addLogoutHandler(jwtLogoutHandler)
//                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(config -> config
//                                .userService(oAuth2UserService))
//                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                )
//                .oauth2Login(Customizer.withDefaults())
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
            JwtTokenProvider jwtTokenProvider, AccountService accountService) {
        return new JwtAuthenticationCheckFilter(jwtTokenProvider, accountService);
    }

    @Bean
    public JwtLogoutHandler jwtLogoutHandler(JwtTokenProvider jwtTokenProvider, AccountService accountService) {
        return new JwtLogoutHandler(jwtTokenProvider, accountService);
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
    }

}
