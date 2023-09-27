package com.example.social.config.oauth2.provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderID();
    String getEmail();
    String getName();
    Map<String, Object> getAttributes();
}
