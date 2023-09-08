package com.example.social.config.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;

// PrincipalDetails의 정보를 가지고 JWT를 만들어준다.
// 이곳에서 JWT를 검증하는 메소드도 존재한다.
@Log4j2
@Component
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.access.expiration}")
    private long accessTokenTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenTime;

    private Key key;

    public JwtProvider(@Value("${jwt.secret_key}") String secretKey) {
        byte[] secretByte = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByte);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메소드
    public
}
