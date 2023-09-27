package com.example.social.entity;

import com.example.social.domain.TokenDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "token")
@Table
@ToString
@Getter
@NoArgsConstructor
public class TokenEntity {
    @Id @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;

    @Builder
    public TokenEntity(Long id,
                       String grantType,
                       String accessToken,
                       String refreshToken,
                       String memberEmail) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
    }

    public static TokenEntity toTokenEntity(TokenDTO token) {
        return TokenEntity.builder()
                .id(token.getId())
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .memberEmail(token.getMemberEmail())
                .build();
    }
}
