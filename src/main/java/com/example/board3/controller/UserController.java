package com.example.board3.controller;

import com.example.board3.domain.MemberDTO;
import com.example.board3.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "user", description = "유저 API")
public class UserController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Tag(name = "user")
    @Operation(summary = "회원가입 API", description = "회원가입하는 API입니다.")
    @PostMapping("/")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO) {

        String userPw = memberDTO.getUserPw();
        String encodePw = bCryptPasswordEncoder.encode(userPw);

        MemberDTO member = MemberDTO.builder()
                .userEmail(memberDTO.getUserEmail())
                .userPw(encodePw)
                .userName(memberDTO.getUserName())
                .build();

        if (member != null) {
            log.info("member = " + member);
            if(memberService.join(member)) {
                return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Tag(name = "user")
    @Operation(summary = "상세정보", description = "유저에 대한 상세정보를 볼 수 있습니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        MemberDTO user = memberService.getUser(userId);

        if(user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "user")
    @Operation(summary = "전체 조회", description = "모든 유저에 대한 정보를 볼 수 있습니다. ")
    @GetMapping("/allUsers")
    public ResponseEntity<List<MemberDTO>> getAllUser() {
        List<MemberDTO> allUser = memberService.getAllUser();

        if (allUser != null) {
            for (MemberDTO m: allUser
                 ) {
                log.info(String.valueOf(m.getUserId()));
                log.info(String.valueOf(m.getUserEmail()));
                log.info(String.valueOf(m.getUserPw()));
                log.info(String.valueOf(m.getUserName()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(allUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "user")
    @Operation(summary = "로그인", description = "로그인하는 API입니다.")
    @PostMapping("/loginUser")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, HttpSession httpSession) {
        MemberDTO login = memberService.login(memberDTO.getUserEmail(), memberDTO.getUserPw());

        if(login != null) {
            httpSession.setAttribute("userId" , login.getUserEmail());

            String userId = (String) httpSession.getAttribute("userId");

            log.info("userId : " + userId);

            ResponseEntity.ok().body(userId);
        }
        return ResponseEntity.notFound().build();
    }

    @Tag(name = "user")
    @Operation(summary = "회원수정", description = "회원정보를 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable Long userId, @RequestBody MemberDTO memberDTO) {
        Long checkId = memberDTO.getUserId();

        if(checkId == userId) {
            memberService.update(memberDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(memberDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Tag(name = "user")
    @Operation(summary = "아이디 삭제", description = "아이디를 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        memberService.remove(userId);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "user")
    @Operation(summary = "이메일 체크", description = "아이디가 이미 존재하는지 체크합니다.")
    @PostMapping("/email-check/{userEmail}")
    public int emailCheck(@PathVariable String userEmail) {
        int emailCheck = memberService.emailCheck(userEmail);

        if(emailCheck != 0) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        } else {
            return 0;
        }
    }
}
