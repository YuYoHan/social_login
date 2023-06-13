package com.example.board3.domain.jwt;

import lombok.*;

// 클라이언트에 토큰을 보내기 위한 DTO

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    // JWT에 대한 인증 타입으로 여기서는 Bearer를 사용한다.
    // 이후 HTTP 헤더에 prefix로 붙여주는 타입이기도 하다.
    private String grantType;
    private String accessToken;
    private String refreshToken;
        }
