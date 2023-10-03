package com.example.social.domain;

import com.example.social.entity.MemberEntity;
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
    private String password;
    private String userName;
    private String provider;
    private String providerId;
    private String nickName;
    private Role role;


    @Builder
    public MemberDTO(Long memberId,
                     String email,
                     String password,
                     String userName,
                     String provider,
                     String providerId,
                     String nickName,
                     Role role) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.provider = provider;
        this.providerId = providerId;
        this.nickName = nickName;
        this.role = role;
    }

    public static MemberDTO toMemberDTO(MemberEntity member) {
        return MemberDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .userName(member.getUserName())
                .password(member.getPassword())
                .role(member.getRole())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .nickName(member.getNickName())
                .build();
    }
}
