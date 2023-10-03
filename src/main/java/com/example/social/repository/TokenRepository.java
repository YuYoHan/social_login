package com.example.social.repository;

import com.example.social.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByMemberEmail(String userEmail);
    TokenEntity findByRefreshToken(String refreshToken);
}
