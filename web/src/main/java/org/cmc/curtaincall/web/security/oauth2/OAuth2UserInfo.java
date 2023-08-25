package org.cmc.curtaincall.web.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getProviderId();

    String ofUsername();

    static OAuth2UserInfo of(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if ("kakao".equals(registrationId)) {
            return new KakaoUserInfo(attributes.get("id").toString());
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider 입니다. provider = " + registrationId);
        }
    }

    static OAuth2UserInfo of(String registrationId, String providerId) {
        if ("kakao".equals(registrationId)) {
            return new KakaoUserInfo(providerId);
        } else if ("apple".equals(registrationId)) {
            return new AppleUserInfo(providerId);
        } else {
            throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider 입니다. provider = " + registrationId);
        }
    }

}
