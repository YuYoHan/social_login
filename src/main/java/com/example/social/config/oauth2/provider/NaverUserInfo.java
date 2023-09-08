package com.example.social.config.oauth2.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    // PrincipalOauth2UserService에서 new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"))로
    // Oauth2 네이버 로그인 정보를 받아온다.
    public NaverUserInfo(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
