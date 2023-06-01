package com.example.board3.mapper;

import com.example.board3.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 회원가입
    int join(MemberDTO memberDTO);
    // 유저 검색
    MemberDTO getUser(Long userId);
    // 모든 유저 검색
    List<MemberDTO> getAllUser();

    MemberDTO findByName(String userName);

    MemberDTO login(@Param("userId")String userEmail, @Param("userPw") String userPw);

    void update(MemberDTO memberDTO);

    void delete(Long userId);

    int emailCheck(String userEmail);

}
