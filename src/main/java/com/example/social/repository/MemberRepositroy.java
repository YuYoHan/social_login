package com.example.social.repository;

import com.example.social.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositroy extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);
    MemberEntity findByProviderId(String providerId);
}
