package com.example.board3.controller;

import com.example.board3.domain.member.MemberDTO;
import com.example.board3.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "member", description = "유저 관련 기능")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    @PostMapping("/")
    @Tag(name = "member")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<?> join(@Validated @RequestBody MemberDTO memberDTO,
                                       BindingResult result) throws Exception {

        if(result.hasErrors()) {
            log.info("error : " + result.hasErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
        }

        try {
            ResponseEntity<?> user = memberService.createUser(memberDTO);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            log.info("error : " + e.getMessage());
            throw new Exception("에러가 발생했습니다.");
        }
    }
    
}
