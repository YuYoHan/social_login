package com.example.social.config.security;

import com.example.social.config.jwt.JwtAuthenticationFilter;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.config.oauth2.verifirer.GoogleOAuth2UserService;
import com.example.social.config.oauth2.verifirer.NaverOAuth2UserService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// SecurityConfigurerAdapter 클래스를 확장하여 Jwt 기반 보안 구성을 정의
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtProvider jwtProvider;
    private final GoogleOAuth2UserService googleOAuth2UserService;
    // OAuth2 클라이언트 등록 정보가 포함된 객체
    private final ClientRegistration clientRegistration;
    private final NaverOAuth2UserService naverOAuth2UserService;

    public JwtSecurityConfig(JwtProvider jwtProvider,
                             GoogleOAuth2UserService googleOAuth2UserService,
                             ClientRegistration clientRegistration,
                             NaverOAuth2UserService naverOAuth2UserService) {
        this.jwtProvider = jwtProvider;
        this.googleOAuth2UserService = googleOAuth2UserService;
        this.clientRegistration = clientRegistration;
        this.naverOAuth2UserService = naverOAuth2UserService;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        // JwtAuthenticationFilter가 일반 로그인에 대한 토큰 검증을 처리
        // JwtAuthenticationFilter는 Jwt 토큰을 사용하여 사용자의 인증을 처리하는 필터
        // 이 필터는 일반 로그인 요청에서 Jwt 토큰을 검증하고 사용자를 인증합니다.
        // 이 필터를 UsernamePasswordAuthenticationFilter 앞에 추가하여 Jwt 토큰 검증을 먼저 수행하도록 합니다.
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(jwtProvider);
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        // OAuth2 프로바이더(Google 또는 Naver)로부터 제공된 OAuth2 토큰을 사용하여 사용자를 인증하는 역할을 합니다.
        // 이 필터를 JwtAuthenticationFilter 앞에 추가하여 Jwt 토큰 검증 후 OAuth2 토큰 인증을 수행하도록 합니다.
//        builder.addFilterBefore(
//                new OAuth2TokenAuthentication(
//                        googleOAuth2UserService,
//                        clientRegistration,
//                        naverOAuth2UserService), JwtAuthenticationFilter.class);
    }
}
