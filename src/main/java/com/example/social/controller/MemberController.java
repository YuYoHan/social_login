package com.example.social.controller;

import com.example.social.config.security.PrincipalDetails;
import com.example.social.domain.MemberDTO;
import com.example.social.domain.TokenDTO;
import com.example.social.service.MemberService;
import com.example.social.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;


    // 회원가입
    @PostMapping("/")
    public ResponseEntity<?> signUp(@RequestBody MemberDTO memberDTO) throws Exception {
        try {
            ResponseEntity<?> responseEntity = memberService.signUp(memberDTO);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO) throws Exception {
        try {
            String email = memberDTO.getEmail();
            String passwrod = memberDTO.getPassword();
            ResponseEntity<?> responseEntity = memberService.signIn(email, passwrod);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // 회원정보 수정
    @PutMapping("/api/v1/users")
    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        try {
            log.info("userDetails : " + userDetails);
            // 검증과 유효성이 끝난 토큰을 SecurityContext 에 저장하면
            // @AuthenticationPrincipal UserDetails userDetails 으로 받아오고 사용
            // zxzz45@naver.com 이런식으로 된다.
            String userEmail = userDetails.getUsername();
            log.info("email : " + userEmail);
            MemberDTO update = memberService.update(memberDTO, userEmail);
            return ResponseEntity.ok().body(update);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }

    // 소셜 로그인
//    @GetMapping("/api/v1/users/social-login/{providerId}")
//    public ResponseEntity<?> socialLogin(@PathVariable String providerId) {
//        try {
//            log.info("providerId : " + providerId);
//            ResponseEntity<?> responseEntity = memberService.socialLogin(providerId);
//            return ResponseEntity.ok().body(responseEntity);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("정보를 가지고 올 수 가 없습니다.");
//        }
//    }

    // refresh로 access 토큰 재발급
    // @RequsetHeader"Authorization")은 Authorization 헤더에서 값을 추출합니다.
    // 일반적으로 리프레시 토큰은 Authorization 헤더의 값으로 전달되며,
    // Bearer <token> 형식을 따르는 경우가 일반적입니다. 여기서 <token> 부분이 실제 리프레시 토큰입니다
    // 로 추출하면 다음과 같이 문자열로 나온다.
    // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
    @PostMapping("/refresh")
    @Tag(name = "member")
    @Operation(summary = "access token 발급", description = "refresh token을 받으면 access token을 반환해줍니다.")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) throws Exception {
        try {
            if(token != null) {
                ResponseEntity<TokenDTO> accessToken = refreshTokenService.createAccessToken(token);
                return ResponseEntity.ok().body(accessToken);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
