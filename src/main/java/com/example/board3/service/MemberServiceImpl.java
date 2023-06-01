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

    @Override
    public MemberDTO findByName(String userName) {
        return memberMapper.findByName(userName);
    }

    @Override
    public MemberDTO login(String userEmail, String userPw) {
        return memberMapper.login(userEmail, userPw);
    }

    @Override
    public void update(MemberDTO memberDTO) {
        memberMapper.update(memberDTO);
    }

    @Override
    public void remove(Long userId) {
        memberMapper.delete(userId);
    }

    @Override
    public int emailCheck(String userEmail) {
        return memberMapper.emailCheck(userEmail);
    }
}
