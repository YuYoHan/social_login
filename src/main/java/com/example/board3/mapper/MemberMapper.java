package com.example.board3.mapper;

import com.example.board3.domain.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int join(MemberDTO memberDTO);
}
