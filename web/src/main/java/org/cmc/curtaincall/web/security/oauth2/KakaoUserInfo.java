package org.cmc.curtaincall.web.security.oauth2;

import lombok.Builder;

public class KakaoUserInfo implements OAuth2UserInfo {

    private static final OAuth2Provider provider = OAuth2Provider.KAKAO;

    private final String providerId;

    @Builder
    public KakaoUserInfo(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public OAuth2Provider getProvider() {
        return provider;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String ofUsername() {
        return getProvider() + "-" + getProviderId();
    }
}
