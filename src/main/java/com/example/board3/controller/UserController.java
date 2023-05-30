package com.example.board3.controller;

import com.example.board3.domain.MemberDTO;
import com.example.board3.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO) {

        String userPw = memberDTO.getUserPw();
        String encodePw = bCryptPasswordEncoder.encode(userPw);

        MemberDTO member = MemberDTO.builder()
                .userId(memberDTO.getUserId())
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
}
