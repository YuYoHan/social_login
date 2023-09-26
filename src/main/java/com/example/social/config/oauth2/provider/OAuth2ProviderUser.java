package com.example.social.config.oauth2.provider;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


public abstract class OAuth2ProviderUser implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;

    public OAuth2ProviderUser(Map<String, Object> attributes,
                              OAuth2User oAuth2User,
                              ClientRegistration clientRegistration) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }


    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
