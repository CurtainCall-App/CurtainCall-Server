package org.cmc.curtaincall.web.security.service;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.stream.Collectors;

public class UsernameService {

    private final Map<String, String> issuerUriToProviderName;

    public UsernameService(OAuth2ClientProperties properties) {
        this.issuerUriToProviderName = properties.getProvider().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri() != null)
                .collect(Collectors.toUnmodifiableMap(
                        entry -> entry.getValue().getIssuerUri(),
                        Map.Entry::getKey
                ));
    }

    public String getUsername(Authentication authentication) {
        String name = authentication.getName();
        String providerName = getProviderName(authentication);
        return providerName.toUpperCase() + "-" + name;
    }

    private String getProviderName(Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken oauth2AuthenticationToken) {
            return oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        } else if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return convertIssuerUriToProviderName((String) jwtAuthenticationToken.getTokenAttributes().get("iss"));
        } else {
            throw new IllegalArgumentException("지원되지 않은 Authentication 타입. " + authentication.getClass());
        }
    }

    private String convertIssuerUriToProviderName(String issuerUri) {
        return issuerUriToProviderName.get(issuerUri);
    }
}
