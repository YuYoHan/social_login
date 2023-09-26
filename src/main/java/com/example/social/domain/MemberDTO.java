package com.example.social.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    private String email;
    private String passwrod;
    private String userName;
    private String provider;
    private String providerId;
    private Role role;


    @Builder
    public MemberDTO(Long memberId, String email, String passwrod, String userName, String provider, String providerId, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.passwrod = passwrod;
        this.userName = userName;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }
}
