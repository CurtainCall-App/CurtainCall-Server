package org.cmc.curtaincall.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cmc.curtaincall.web.security.request.TokenLoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Map;

public class OAuth2TokenLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String FILTER_PROCESSES_URI = "/login/oauth2/token/*";

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final Map<String, String> issuerToProvider;

    private final ObjectMapper objectMapper;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = new DefaultOAuth2UserService();

    public OAuth2TokenLoginAuthenticationFilter(
            final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver,
            final ClientRegistrationRepository clientRegistrationRepository,
            final Map<String, String> issuerToProvider,
            final ObjectMapper objectMapper
    ) {
        super(FILTER_PROCESSES_URI);
        setAuthenticationManager(authentication -> {
            throw new AuthenticationServiceException("Cannot authenticate " + authentication);
        });
        this.authenticationManagerResolver = authenticationManagerResolver;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.issuerToProvider = issuerToProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request, final HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        final String provider = request.getServletPath().split("/")[4];
        final TokenLoginRequest loginRequest = objectMapper.readValue(request.getReader(), TokenLoginRequest.class);
        if ("naver".equals(provider)) {
            final ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(provider);
            final OAuth2User oAuth2User = oauth2UserService.loadUser(new OAuth2UserRequest(clientRegistration, new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER, loginRequest.token(), null, null
            )));
            return UsernamePasswordAuthenticationToken.authenticated(
                    provider + "-" + oAuth2User.getName(), null, AuthorityUtils.NO_AUTHORITIES
            );
        } else {
            final BearerTokenAuthenticationToken authenticationToken = new BearerTokenAuthenticationToken(
                    loginRequest.token());
            final AuthenticationManager authenticationManager = authenticationManagerResolver.resolve(request);
            final JwtAuthenticationToken authentication = (JwtAuthenticationToken) authenticationManager
                    .authenticate(authenticationToken);
            final String issuer = (String) authentication.getTokenAttributes().get(JwtClaimNames.ISS);
            if (!provider.equals(issuerToProvider.get(issuer))) {
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST));
            }

            return UsernamePasswordAuthenticationToken.authenticated(
                    provider + "-" + authentication.getName(), null, AuthorityUtils.NO_AUTHORITIES
            );
        }

    }
}
