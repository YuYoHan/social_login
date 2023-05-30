package com.example.board3.controller;

import com.example.board3.domain.MemberDTO;
import com.example.board3.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/")
    public ResponseEntity<String> join(@RequestBody MemberDTO memberDTO) {
        if (memberService.join(memberDTO)) {
            log.info("memberDTO = " + memberDTO);
            return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
