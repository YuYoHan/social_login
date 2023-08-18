package com.example.board3.service;

import com.example.board3.domain.member.MemberDTO;
import com.example.board3.entity.member.AddressEntity;
import com.example.board3.entity.member.MemberEntity;
import com.example.board3.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseEntity<?> createUser(MemberDTO memberDTO) throws Exception {
        MemberEntity findEmail = memberRepository.findByUserEmail(memberDTO.getUserEmail());

        if(findEmail != null) {
            log.info("이미 가입된 회원입니다.");
            return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
        }

        MemberEntity member = MemberEntity.builder()
                .userEmail(memberDTO.getUserEmail())
                .userPw(passwordEncoder.encode(memberDTO.getUserPw()))
                .userName(memberDTO.getUserName())
                .nickName(memberDTO.getNickName())
                .address(AddressEntity.builder()
                        .userAddr(memberDTO.getAddressDTO().getUserAddr())
                        .userAddrDetails(memberDTO.getAddressDTO().getUserAddrDetail())
                        .userAddrEtc(memberDTO.getAddressDTO().getUserAddrEtc())
                        .build()).build();

        log.info("member : " + member);
        return ResponseEntity.ok().body(member);
    }
}
