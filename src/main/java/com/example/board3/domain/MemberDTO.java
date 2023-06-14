package com.example.board3.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class MemberDTO {
    @Schema(description = "유저번호", example = "1", required = true)
    private Long userId;
    @Schema(description = "이메일", example = "abc@gmail.com", required = true)
    private String userEmail;
    @Schema(description = "비밀번호")
    private String userPw;
    @Schema(description = "유저이름")
    private String userName;
    @Schema(description = "OAuth 제공자")
    private String provider;
    @Schema(description = "OAuth 제공아이디")
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
