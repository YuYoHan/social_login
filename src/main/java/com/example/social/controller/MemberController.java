package com.example.social.controller;

import com.example.social.config.security.PrincipalDetails;
import com.example.social.domain.MemberDTO;
import com.example.social.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;
    private final PrincipalDetails principalDetails;


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
    @GetMapping("/api/v1/users/social-signUp")
    public ResponseEntity<?> socialLogin(@PathVariable String providerId) {
        try {
            log.info("providerId : " + providerId);
            ResponseEntity<?> responseEntity = memberService.socialLogin(providerId);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("정보를 가지고 올 수 가 없습니다.");
        }
    }
    @PostMapping("/api/v1/users/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody MemberDTO memberDTO) throws Exception {
        try {
            // 여기에 추가 정보가 담겨져 있다.
            log.info("소셜 로그인 추가정보 : " + memberDTO);
            ResponseEntity<?> responseEntity = memberService.socialLogin2(memberDTO);
            return  ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }
}
