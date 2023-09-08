package com.example.social.service;

import com.example.social.config.jwt.JwtAuthenticationFilter;
import com.example.social.config.jwt.JwtProvider;
import com.example.social.domain.MemberDTO;
import com.example.social.domain.Role;
import com.example.social.domain.TokenDTO;
import com.example.social.entity.AddressEntity;
import com.example.social.entity.MemberEntity;
import com.example.social.entity.TokenEntity;
import com.example.social.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
// 비즈니스 로직을 담당하는 서비스 계층 클래스에
// @Transactional 어노테이션을 선언합니다.
// 로직을 처리하다가 에러가 발생하면
// 변경된 데이터 로직을 처리하기 전으로 콜백해줍니다.
@Transactional
// 빈 주입 방법중 한 개인데
// @NonNull 이나 final 붙은 필드에 생성자를 생성
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    public ResponseEntity<?> signUp(MemberDTO member) {
        try {
            MemberEntity findUser = memberRepository.findByUserEmail(member.getUserEmail());

            if(findUser != null) {
                return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
            } else  {
                MemberEntity memberEntity = MemberEntity.builder()
                        .userEmail(member.getUserEmail())
                        .userPw(passwordEncoder.encode(member.getUserPw()))
                        .userName(member.getUserName())
                        .nickName(member.getNickName())
                        .role(member.getRole())
                        .address(AddressEntity.builder()
                                .userAddr(member.getAddressDTO().getUserAddr())
                                .userAddrDetail(member.getAddressDTO().getUserAddrDetails())
                                .userAddrEtc(member.getAddressDTO().getUserAddrEtc())
                                .build()).build();

                log.info("member : " + member);
                memberRepository.save(memberEntity);
                MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
                return ResponseEntity.ok().body(memberDTO);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 회원 조회
    public ResponseEntity<MemberDTO> search(Long userId) {
        MemberEntity memberEntity = memberRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
        return ResponseEntity.ok().body(memberDTO);
    }

    // 로그인
    public ResponseEntity<?> login(String userEmail, String userPw) {
        MemberEntity findUser = memberRepository.findByUserEmail(userEmail);
        log.info("findUser : " + findUser);

        if(findUser != null) {
            // 사용자가 입력한 패스워드를 암호화하여 사용자 정보와 비교
            if(passwordEncoder.matches(userPw, findUser.getUserPw())) {
                // UsernamePasswordAuthenticationToken은 Spring Security에서
                // 사용자의 이메일과 비밀번호를 이용하여 인증을 진행하기 위해 제공되는 클래스
                // 이후에는 생성된 authentication 객체를 AuthenticationManager를 이용하여 인증을 진행합니다.
                // AuthenticationManager는 인증을 담당하는 Spring Security 의 중요한 인터페이스로, 실제로 사용자의 인증 과정을 처리합니다.
                // AuthenticationManager를 사용하여 사용자가 입력한 이메일과 비밀번호가 올바른지 검증하고,
                // 인증에 성공하면 해당 사용자에 대한 Authentication 객체를 반환합니다. 인증에 실패하면 예외를 발생시킵니다.
                // 인증은 토큰을 서버로 전달하고, 서버에서 해당 토큰을 검증하여 사용자를 인증하는 단계에서 이루어집니다.
                // 즉, Authentication 객체를 생성하고, 해당 객체를 SecurityContext에 저장하게 되면,
                // 인증이 완료되지 않은 상태에서 사용자 정보를 가지는 인증 객체가 저장됩니다.
                // 이후 검증 시에는 해당 인증 객체를 기반으로 다시 UsernamePasswordAuthenticationToken을 생성하여
                // 인증 상태를 true로 설정하는 것이 가능합니다.
                Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail, userPw);
                //  UsernamePasswordAuthenticationToken
                //  [Principal=zxzz45@naver.com, Credentials=[PROTECTED], Authenticated=false, Details=null, Granted Authorities=[]]
                // 여기서 Authenticated=false는 아직 정상임
                // 이 시점에서는 아직 실제로 인증이 이루어지지 않았기 때문에 Authenticated 속성은 false로 설정
                // 인증 과정은 AuthenticationManager와 AuthenticationProvider에서 이루어지며,
                // 인증이 성공하면 Authentication 객체의 isAuthenticated() 속성이 true로 변경됩니다.
                log.info("authentication in MemberService : " + authentication);

                List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);
                TokenDTO tokenDTO = jwtProvider.crateToken(authentication, authoritiesForUser);

                TokenEntity findToken = tokenRepository.findByUserEmail(tokenDTO.getUserEmail());

                // 사용자에게 이미 토큰이 할당되어 있는지 확인
                if(findToken != null) {
                    log.info("이미 토큰이 발급되어 있습니다.");
                    // 기존의 토큰을 업데이트 합니다.
                    tokenDTO  = TokenDTO.builder()
                            .id(findToken.getId())
                            .grantType(tokenDTO.getGrantType())
                            .accessToken(tokenDTO.getAccessToken())
                            .accessTokenTime(tokenDTO.getAccessTokenTime())
                            .refreshToken(tokenDTO.getRefreshToken())
                            .refreshTokenTime(tokenDTO.getRefreshTokenTime())
                            .build();

                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenDTO);
                    tokenRepository.save(tokenEntity);
                } else {
                    log.info("발급한 토큰이 없습니다.");
                    tokenDTO = TokenDTO.builder()
                            .grantType(tokenDTO.getGrantType())
                            .accessToken(tokenDTO.getAccessToken())
                            .accessTokenTime(tokenDTO.getAccessTokenTime())
                            .refreshToken(tokenDTO.getRefreshToken())
                            .refreshTokenTime(tokenDTO.getRefreshTokenTime())
                            .build();
                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenDTO);
                    tokenRepository.save(tokenEntity);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(JwtAuthenticationFilter.HEADER_AUTHORIZATION, "Bearer " + tokenDTO);
                return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }


    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity findUser) {
        // 예시: 데이터베이스에서 사용자의 권한 정보를 조회하는 로직을 구현
        // member 객체를 이용하여 데이터베이스에서 사용자의 권한 정보를 조회하는 예시로 대체합니다.
        Role role = findUser.getRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("role in MemberService : " + role.name());
        log.info("authorities in MemberService : " + authorities);
        return authorities;
    }

    // 회원 정보 수정
    public ResponseEntity<?> update(MemberDTO member, String userEmail) {
        MemberEntity findUser = memberRepository.findByUserEmail(userEmail);

        MemberEntity memberEntity = MemberEntity.builder()
                .userId(findUser.getUserId())
                .userEmail(findUser.getUserEmail())
                .userPw(passwordEncoder.encode(member.getUserPw()))
                .userName(member.getUserName())
                .nickName(member.getNickName())
                .role(findUser.getRole())
                .address(AddressEntity.builder()
                        .userAddr(member.getAddressDTO().getUserAddr())
                        .userAddrDetail(member.getAddressDTO().getUserAddrDetails())
                        .userAddrEtc(member.getAddressDTO().getUserAddrEtc())
                        .build()).build();

        log.info("member : " + memberEntity);
        MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
        return ResponseEntity.ok().body(memberDTO);
    }

    // 회원 탈퇴
    public String remove(Long userId) {
        memberRepository.deleteById(userId);
        return "회원을 탈퇴했습니다.";
    }

    // 중복 체크
    public String emailCheck(String userEmail) {
        MemberEntity findEmail = memberRepository.findByUserEmail(userEmail);

        if(findEmail != null) {
            return "중복된 이메일입니다.";
        } else {
            return "사용가능한 이메일입니다.";
        }
    }
}
