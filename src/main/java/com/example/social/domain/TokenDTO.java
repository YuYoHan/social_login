package com.example.social.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class TokenDTO {
    private Long id;
    private String grantType;
    private String accessToken;
    private Date accessTokenTime;
    private String refreshToken;
    private Date refreshTokenTime;
    private String userEmail;

    @Builder
    public TokenDTO(Long id,
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
}
