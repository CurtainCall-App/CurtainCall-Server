package org.cmc.curtaincall.web.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.UserWithMemberId;
import org.cmc.curtaincall.web.security.service.AccountService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationCheckFilter extends OncePerRequestFilter {

    private static final String OAUTH2_PASSWORD = "OAUTH2-ACCOUNT";

    private final JwtTokenProvider jwtTokenProvider;

    private final AccountService accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getSubject(token);
            UserDetails user = User.withUsername(username)
                    .authorities(AuthorityUtils.NO_AUTHORITIES)
                    .password(OAUTH2_PASSWORD)
                    .build();
            Long memberId = accountService.getMemberId(username);
            UserWithMemberId userWithMemberId = new UserWithMemberId(user, memberId);
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    userWithMemberId, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
