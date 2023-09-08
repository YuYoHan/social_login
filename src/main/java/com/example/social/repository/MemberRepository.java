package com.example.social.repository;

import com.example.social.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 회원 가입 시 중복된 회원이 있는지 검사하기 위해서
    // 이메일로 회원을 검사할 수 있도록 쿼리 메소드를 작성
    MemberEntity findByUserEmail(String email);
}
