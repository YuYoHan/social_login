package com.example.social.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class TokenDTO {
    private Long id;
    private String grantType;
    private String accessToken;
    private String accessTokenTime;
    private String refreshToken;
    private String refreshTokenTime;
    private String userEmail;

    @Builder
    public TokenDTO(Long id,
                    String grantType,
                    String accessToken,
                    String accessTokenTime,
                    String refreshToken,
                    String refreshTokenTime,
                    String userEmail) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenTime = accessTokenTime;
        this.refreshToken = refreshToken;
        this.refreshTokenTime = refreshTokenTime;
        this.userEmail = userEmail;
    }
}
