package com.example.board3.entity.member;

import com.example.board3.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "members")
@Getter
@ToString
@NoArgsConstructor
public class MemberEntity {
    @Id @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_pw", nullable = false)
    private String userPw;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "user_addr", nullable = false)
    private AddressEntity address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public MemberEntity(Long userId,
                        String userName,
                        String userEmail,
                        String userPw,
                        String nickName,
                        AddressEntity address,
                        Role role) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.nickName = nickName;
        this.address = address;
        this.role = role;
    }
}
