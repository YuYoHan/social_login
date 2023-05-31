package com.example.board3.config.jwt;

import com.example.board3.domain.MemberDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProperties;


    public String generateToken(MemberDTO memberDTO, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), memberDTO);
    }

    // 1. JWT 토큰 생성 메서드
    /*
    *   인자는 만료시간, 유저 정보를 받습니다.
    *
    * */
    private String makeToken(Date expiry, MemberDTO memberDTO) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 type : JWT
                // 내용 iss : 프로퍼티 파일에서 설정한 값
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)   // 내용 iat : 현재 시간
                .setExpiration(expiry)  // 내용 exp : expiry 멤버 변수값
                .setSubject(memberDTO.getUserEmail())   // 내용 sub : 유저의 이메일
                .claim("id", memberDTO.getUserId()) // 클레임 id : 유저 id
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 2. JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    // 비밀 값으로 복호화
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 3. 토큰 기반으로 인증 정보를 가져오는 메소드
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new User(claims.getSubject(),"",authorities), token, authorities);
    }

    // 4. 토큰 기반으로 유저 ID를 가져오는 메소드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return  claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }


}
