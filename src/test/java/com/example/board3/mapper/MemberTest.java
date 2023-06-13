package com.example.board3.mapper;

import com.example.board3.domain.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MemberTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        String email = "zxzz11@naver.com";
        String password = "1234";
        String name = "테스터";

        MemberDTO member = MemberDTO.builder()
                .userEmail(email)
                .userPw(password)
                .userName(name)
                .build();

        if(memberMapper.join(member) == 1) {
            log.info(String.valueOf(member));

            Assertions.assertThat(member.getUserEmail()).isEqualTo(email);
            Assertions.assertThat(member.getUserPw()).isEqualTo(password);
            Assertions.assertThat(member.getUserName()).isEqualTo(name);
        }
    }

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest() {
        MemberDTO member2 = MemberDTO.builder()
                .userEmail("zxzz11@naver.com")
                .userPw("1234")
                .build();

        MemberDTO login = memberMapper.login(member2.getUserEmail(), member2.getUserPw());
        Assertions.assertThat(member2.getUserEmail()).isEqualTo(login.getUserEmail());
        Assertions.assertThat(member2.getUserPw()).isEqualTo(login.getUserPw());
    }

    @Test
    @DisplayName("이메일 중복 체크 테스트")
    void emailCheckTest() {
        MemberDTO member3 = MemberDTO.builder()
                .userEmail("zxzz12@naver.com")
                .build();

        int i = memberMapper.emailCheck(member3.getUserEmail());

        if(i == 1) {
            log.error("아이디가 중복입니다.");
            Assertions.assertThat(member3.getUserEmail()).isEqualTo("zxzz45@naver.com");
        }
    }
}
