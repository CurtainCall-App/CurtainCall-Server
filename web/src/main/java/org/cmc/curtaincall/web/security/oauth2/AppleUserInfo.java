package org.cmc.curtaincall.web.security.oauth2;

public class AppleUserInfo implements OAuth2UserInfo {

    private static final OAuth2Provider provider = OAuth2Provider.APPLE;

    private final String providerId;

    public AppleUserInfo(String providerId) {
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
