package com.example.social.config.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

// PrincipalDetails 클래스는 UserDetails 인터페이스를 구현하여 사용자의 정보와 권한을 저장하는 역할을 하고 있습니다.
// 여기서 JwtProvider 에 정보를 줘서 토큰을 생성하게 한다.
// UserDetails → 일반 로그인
// OAuth2User → 소셜 로그인
@Setter
@Getter
@ToString
@Log4j2
@NoArgsConstructor
@Component
public class PrincipalDetails implements UserDetails, OAuth2User {
    // 일반 로그인 정보를 저장하기 위한 필드
    private MemberEntity member;
}
