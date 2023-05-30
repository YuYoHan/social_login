package com.example.board3.service;

import com.example.board3.domain.MemberDTO;
import com.example.board3.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;

    @Override
    public boolean join(MemberDTO memberDTO) {
        return memberMapper.join(memberDTO) == 1;
    }

    @Override
    public MemberDTO getUser(Long userId) {
        return memberMapper.getUser(userId);
    }

    @Override
    public List<MemberDTO> getAllUser() {
        return memberMapper.getAllUser();
    }
}
