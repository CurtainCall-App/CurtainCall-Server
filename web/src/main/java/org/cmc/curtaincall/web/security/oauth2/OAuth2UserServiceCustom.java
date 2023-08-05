package org.cmc.curtaincall.web.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceCustom extends DefaultOAuth2UserService {

    private static final String NAME_ATTRIBUTE_KEY = "username";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(userRequest, user.getAttributes());
        String username = userInfo.ofUsername();
        Map<String, Object> attributes = Map.of(
                NAME_ATTRIBUTE_KEY, username
        );
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        return new DefaultOAuth2User(authorities, attributes, NAME_ATTRIBUTE_KEY);
    }

}
