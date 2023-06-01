package com.example.board3.service;

import com.example.board3.domain.MemberDTO;

import java.util.List;

public interface MemberService {
    // 회원가입
    boolean join(MemberDTO memberDTO);
    // 검색
    MemberDTO getUser(Long userId);
    // 모든 회원 검색
    List<MemberDTO> getAllUser();

    MemberDTO findByName(String userName);

    MemberDTO login(String userEmail, String userPw);

    void update(MemberDTO memberDTO);

    void remove(Long userId);

    int emailCheck(String userEmail);
}
