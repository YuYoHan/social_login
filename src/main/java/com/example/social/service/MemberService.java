package com.example.social.service;

import com.example.social.config.jwt.JwtAuthenticationFilter;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.domain.MemberDTO;
import com.example.social.domain.Role;
import com.example.social.domain.TokenDTO;
import com.example.social.entity.MemberEntity;
import com.example.social.entity.TokenEntity;
import com.example.social.repository.MemberRepositroy;
import com.example.social.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositroy memberRepositroy;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    public ResponseEntity<?> signUp(MemberDTO memberDTO) {
        MemberEntity findMember = memberRepositroy.findByEmail(memberDTO.getEmail());

        if (findMember == null) {
            MemberEntity memberEntity = MemberEntity.builder()
                    .email(memberDTO.getEmail())
                    .password(passwordEncoder.encode(memberDTO.getPassword()))
                    .userName(memberDTO.getUserName())
                    .role(memberDTO.getRole())
                    .nickName(memberDTO.getNickName())
                    .build();

            MemberEntity save = memberRepositroy.save(memberEntity);
            log.info("member : " + save);
            MemberDTO memberDTO1 = MemberDTO.toMemberDTO(save);
            return ResponseEntity.ok().body(memberDTO1);
        } else {
            log.info("이미 가입된 회원이 있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 회원이 있습니다.");
        }
    }

    // 로그인
    public ResponseEntity<?> signIn(String email, String passwrod) {
        MemberEntity findUser = memberRepositroy.findByEmail(email);

        if (findUser != null) {
            if (passwordEncoder.matches(passwrod, findUser.getPassword())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, passwrod);
                log.info("authentication : " + authentication);

                List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);
                TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser);

                TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                if (findToken == null) {
                    log.info("발급한 토큰이 없습니다.");
                    token = TokenDTO.builder()
                            .grantType(token.getGrantType())
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .memberEmail(token.getMemberEmail())
                            .build();

                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);
                    tokenRepository.save(tokenEntity);
                } else {
                    token = TokenDTO.builder()
                            .id(findToken.getId())
                            .grantType(token.getGrantType())
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .memberEmail(token.getMemberEmail())
                            .build();

                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);
                    tokenRepository.save(tokenEntity);
                }
                HttpHeaders headers = new HttpHeaders();

                headers.add(JwtAuthenticationFilter.HEADER_AUTHORIZATION, "Bearer " + token);
                return new ResponseEntity<>(token, headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }

    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity findUser) {
        Role role = findUser.getRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("권한 : " + role.name());
        return authorities;
    }

//    // 소셜 로그인
//    public ResponseEntity<?> socialLogin(String providerId) {
//        MemberEntity findUser = memberRepositroy.findByProviderId(providerId);
//        TokenEntity findToken = tokenRepository.findByMemberEmail(findUser.getEmail());
//
//        TokenDTO tokenDTO = TokenDTO.builder()
//                .id(findToken.getId())
//                .grantType(findToken.getGrantType())
//                .accessToken(findToken.getAccessToken())
//                .refreshToken(findToken.getRefreshToken())
//                .memberEmail(findToken.getMemberEmail())
//                .build();
//        log.info("token : " + tokenDTO);
//        return ResponseEntity.ok().body(tokenDTO);
//    }


    // 회원정보 수정
    public MemberDTO update(MemberDTO memberDTO, String userEmail) {

        MemberEntity findUser = memberRepositroy.findByEmail(userEmail);
        log.info("findUser : " + findUser);

        // 새로 가입
        if (findUser == null) {
            findUser = MemberEntity.builder()
                    .email(memberDTO.getEmail())
                    .password(passwordEncoder.encode(memberDTO.getPassword()))
                    .role(memberDTO.getRole())
                    .userName(memberDTO.getUserName())
                    .nickName(memberDTO.getNickName())
                    .build();

            memberRepositroy.save(findUser);
            MemberDTO modifyUser = MemberDTO.toMemberDTO(findUser);
            return modifyUser;
        } else {
            // 회원 수정
            findUser = MemberEntity.builder()
                    // id를 식별해서 수정
                    // 이거 없으면 새로 저장하기 됨
                    // findUser꺼를 쓰면 db에 입력된거를 사용하기 때문에
                    // 클라이언트에서 userEmail을 전달하더라도 서버에서 기존 값으로 업데이트가 이루어질 것입니다.
                    // 이렇게 하면 userEmail을 수정하지 못하게 할 수 있습니다.
                    .id(findUser.getId())
                    .email(findUser.getEmail())
                    .password(passwordEncoder.encode(memberDTO.getPassword()))
                    .userName(memberDTO.getUserName())
                    .role(memberDTO.getRole())
                    .nickName(memberDTO.getNickName())
                    .build();

            memberRepositroy.save(findUser);
            // 제대로 DTO 값이 엔티티에 넣어졌는지 확인하기 위해서
            // 엔티티에 넣어주고 다시 DTO 객체로 바꿔서 리턴을 해줬습니다.
            MemberDTO memberDto = MemberDTO.toMemberDTO(findUser);
            log.info("memberDto : " + memberDto);
            return memberDto;
        }
    }
}
