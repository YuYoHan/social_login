package com.example.board3.domain;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private String userId;
    private String userPw;
    private String userName;
}
