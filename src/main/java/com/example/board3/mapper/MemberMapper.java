package com.example.board3.mapper;

import com.example.board3.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 회원가입
    int join(MemberDTO memberDTO);
    // 유저 검색
    MemberDTO getUser(Long userId);
    // 모든 유저 검색
    List<MemberDTO> getAllUser();

}
