package com.example.board3.domain;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class MemberDTO {
    private Long userId;
    private String userEmail;
    private String userPw;
    private String userName;
    private String provider;
    private String providerId;

    @Builder
    public MemberDTO(String userEmail, String userPw, String userName, String provider, String providerId) {
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.userName = userName;
        this.provider = provider;
        this.providerId = providerId;
    }
}
