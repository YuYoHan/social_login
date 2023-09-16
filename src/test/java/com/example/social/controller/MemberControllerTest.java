//package com.example.social.controller;
//
//import com.example.social.domain.TokenDTO;
//import com.example.social.service.MemberService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.test.context.ActiveProfiles;
//import static org.mockito.Mockito.when;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//class MemberControllerTest {
//
//    @Autowired
//    private MemberController memberController;
//
//    @MockBean
//    private MemberService memberService;
//
//    @Test
//    void testSocialLogin() {
//        // Mock 데이터 설정
//        OAuth2User mockOAuth2User = // 생성하려는 Mock OAuth2User
//                when(memberService.createTokenForOAuth2("test@example.com"))
//                        .thenAnswer(invocation -> {
//                            String email = invocation.getArgument(0);
//                            createTokenForOAuth2(email);
//                        });
//    }
//
//    private TokenDTO createTokenForOAuth2(String email) {
//        To
//    }
//}