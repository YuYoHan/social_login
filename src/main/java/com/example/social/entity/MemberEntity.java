package com.example.social.entity;

import com.example.social.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "member")
@Getter
@ToString
@NoArgsConstructor
public class MemberEntity {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_pw")
    private String userPw;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    @Enumerated(EnumType.STRING)
    // ROLE_USER, ROLE_ADMIN
    private Role role;

    // OAuth2 가입할 때를 위해서
    private String provider;
    private String providerId;

    @Embedded
    private AddressEntity address;

    @Builder
    public MemberEntity(
            Long userId,
            String userName,
            String userEmail,
            String userPw,
            String nickName,
            Role role,
            String provider,
            String providerId,
            AddressEntity address) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.nickName = nickName;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.address = address;
    }
}
