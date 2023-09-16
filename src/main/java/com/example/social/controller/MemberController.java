package com.example.social.controller;

import com.example.social.domain.MemberDTO;
import com.example.social.domain.TokenDTO;
import com.example.social.service.MemberService;
import com.example.social.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    // 회원가입
    @PostMapping("/api/v1/users/")
    public ResponseEntity<?> signUp(@Validated @RequestBody MemberDTO member,
                                    BindingResult result) throws Exception {
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
        }

        try {
            ResponseEntity<?> responseEntity = memberService.signUp(member);
            return ResponseEntity.ok().body(responseEntity);
        }catch (Exception e) {
            throw new Exception();
        }
     }

     // 회원 조회
    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<MemberDTO> search(@PathVariable Long userId) throws Exception {
        try {
            ResponseEntity<MemberDTO> search = memberService.search(userId);
            return search;
        } catch (Exception e) {
            throw new EntityNotFoundException("회원이 없습니다.");
        }
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO member) throws Exception {
        log.info("member : " + member);
        try {
            String userEmail = member.getUserEmail();
            String userPw = member.getUserPw();
            return memberService.login(userEmail, userPw);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request,
                         HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication()
                );
        return "로그아웃 했습니다.";
    }

    // 회원정보 수정
    // 일반 로그인시 발급받은 accessToken에서 정보를 가져올 때는
    // @AuthenticationPrincipal UserDetails userDetails이거를 사용한다.
    @PutMapping("/api/v1/users/")
    public ResponseEntity<?> update(@RequestBody MemberDTO member,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        try {
            // 검증과 유효성이 끝난 토큰을 SecurityContext에 저장하면
            // @AuthenticationPrincipal UserDetails userDetails 으로 받아오고 사용
            // zxzz45@naver.com 이런식으로 된다.
            String userEmail = userDetails.getUsername();
            return memberService.update(member, userEmail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }

    // 회원탈퇴
    @DeleteMapping("/api/v1/users/{userId}")
    public String remove(@PathVariable Long userId) {
        String remove = memberService.remove(userId);
        return remove;
    }

    // 중복 체크
    @PostMapping("/api/v1/users/{userEmail}")
    public String emailCheck(@PathVariable String userEmail) {
        log.info("userEmail : " + userEmail);
        return memberService.emailCheck(userEmail);
    }

    // refresh로 access 토큰 재발급
    // @RequsetHeader"Authorization")은 Authorization 헤더에서 값을 추출합니다.
    // 일반적으로 리프레시 토큰은 Authorization 헤더의 값으로 전달되며,
    // Bearer <token> 형식을 따르는 경우가 일반적입니다. 여기서 <token> 부분이 실제 리프레시 토큰입니다
    // 로 추출하면 다음과 같이 문자열로 나온다.
    // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
    @PostMapping("/api/v1/users/refresh")
    public ResponseEntity<?> createAccessToken(@RequestHeader("Authorization") String token) throws Exception {

        try {
            if (token != null) {
                ResponseEntity<TokenDTO> accessToken = refreshTokenService.createAccessToken(token);
                return ResponseEntity.ok().body(accessToken);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 소셜 로그인
    // 소셜 로그인시 발급받은 accessToken에서 정보를 가져올 때는
    // @AuthenticationPrincipal OAuth2User oAuth2User이거를 사용한다.
    @GetMapping("/success-oauth")
    public ResponseEntity<?> getOAuth2UserInfo(OAuth2AuthenticationToken auth2AuthenticationToken)
            throws Exception{
        try {
            OAuth2User user = auth2AuthenticationToken.getPrincipal();
            log.info("principal : " + user);
            String userName = user.getAttribute("name");
            log.info("userName : " + userName);
            String email = user.getAttribute("email");

            log.info("email : " + email);
            ResponseEntity<?> tokenForOAuth2 = memberService.createTokenForOAuth2(email);
            return ResponseEntity.ok().body(tokenForOAuth2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
