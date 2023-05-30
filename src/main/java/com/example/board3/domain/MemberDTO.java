package com.example.board3.domain;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private Long userId;
    private String userEmail;
    private String userPw;
    private String userName;
}
