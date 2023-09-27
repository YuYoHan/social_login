package com.example.social.config.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.Key;

@Log4j2
@Component
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";
    @Value("${jwt.access.expiration}")
    private long accessTokenTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenTime;

    private Key key;

    public JwtProvider(@Value("${jwt.secret_key}") String secret_key) {
        byte[] secretKey = DatatypeConverter.parseBase64Binary(secret_key);
        this.key = Keys.hmacShaKeyFor(secretKey);
    }



}
