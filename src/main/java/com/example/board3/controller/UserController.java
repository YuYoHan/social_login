package com.example.board3.controller;

import com.example.board3.domain.member.MemberDTO;
import com.example.board3.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        MemberDTO user = memberService.getUser(userId);

        if(user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
}
