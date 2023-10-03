package com.example.social.entity;

import com.example.social.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "member")
@Table
@Getter
@NoArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;
    private String providerId;
    private String nickName;

    @Builder
    public MemberEntity(Long id,
                        String email,
                        String password,
                        String userName,
                        Role role,
                        String provider,
                        String providerId,
                        String nickName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.nickName = nickName;
    }
}