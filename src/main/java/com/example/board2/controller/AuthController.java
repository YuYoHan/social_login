package com.example.board2.controller;

import com.example.board2.DTO.ResponseDto;
import com.example.board2.DTO.SignUpDto;
import com.example.board2.DTO.SignUpResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@RequestBody SignUpDto signUpDto) {
        return null;
    }
}
