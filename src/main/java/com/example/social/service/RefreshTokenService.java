package com.example.social.service;

import com.example.social.config.jwt.JwtAuthenticationFilter;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.domain.Role;
import com.example.social.domain.TokenDTO;
import com.example.social.entity.MemberEntity;
import com.example.social.entity.TokenEntity;
import com.example.social.repository.MemberRepositroy;
import com.example.social.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepositroy memberRepository;

    public ResponseEntity<TokenDTO> createAccessToken(String refreshToken) {
        if(jwtProvider.validateToken(refreshToken)) {
            TokenEntity byRefreshToken = tokenRepository.findByRefreshToken(refreshToken);
            // 아이디 추출
            String userEmail = byRefreshToken.getMemberEmail();
            log.info("userEmail : " + userEmail);

            MemberEntity byUserEmail = memberRepository.findByEmail(userEmail);
            log.info("member : " + byUserEmail);

            List<GrantedAuthority> authorities = getAuthoritiesForUser(byUserEmail);

            TokenDTO accessToken = jwtProvider.createAccessToken(userEmail, authorities);
            log.info("accessToken : " + accessToken);

            TokenEntity tokenEntity = TokenEntity.toTokenEntity(accessToken);

            log.info("token : " + tokenEntity);
            tokenRepository.save(tokenEntity);


            HttpHeaders headers = new HttpHeaders();
            headers.add(JwtAuthenticationFilter.HEADER_AUTHORIZATION, "Bearer " + accessToken);

            return new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Unexpected token");
        }
    }

    // 주어진 사용자에 대한 권한 정보를 가져오는 로직을 구현하는 메서드입니다.
    // 이 메서드는 데이터베이스나 다른 저장소에서 사용자의 권한 정보를 조회하고,
    // 해당 권한 정보를 List<GrantedAuthority> 형태로 반환합니다.
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity byUserEmail) {
        // 예시: 데이터베이스에서 사용자의 권한 정보를 조회하는 로직을 구현
        // member 객체를 이용하여 데이터베이스에서 사용자의 권한 정보를 조회하는 예시로 대체합니다.
        Role role = byUserEmail.getRole();  // 사용자의 권한 정보를 가져오는 로직 (예시)

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" +role.name()));
        log.info("role : " + role.name());
        log.info("authorities : " + authorities);
        return authorities;
    }
}
