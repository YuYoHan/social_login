package com.example.social.entity;

import com.example.social.domain.TokenDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "token")
@Getter
@Table
@NoArgsConstructor
@ToString
public class TokenEntity {
    @Id
    @GeneratedValue
    @Column(name = "token_id")
    private Long id;
    private String grantType;
    private String accessToken;
    private Date accessTokenTime;
    private String refreshToken;
    private Date refreshTokenTime;
    private String userEmail;

    @Builder
    public TokenEntity(Long id,
                       String grantType,
                       String accessToken,
                       Date accessTokenTime,
                       String refreshToken,
                       Date refreshTokenTime,
                       String userEmail) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenTime = accessTokenTime;
        this.refreshToken = refreshToken;
        this.refreshTokenTime = refreshTokenTime;
        this.userEmail = userEmail;
    }

    public static TokenEntity toTokenEntity(TokenDTO token) {
        return TokenEntity.builder()
                .id(token.getId())
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .accessTokenTime(token.getAccessTokenTime())
                .refreshToken(token.getRefreshToken())
                .refreshTokenTime(token.getRefreshTokenTime())
                .userEmail(token.getUserEmail())
                .build();
    }
}