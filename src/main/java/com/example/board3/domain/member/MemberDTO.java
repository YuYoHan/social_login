package com.example.board3.domain.member;

import com.example.board3.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    @Schema(description = "유저 번호", example = "1", required = true)
    private Long userId;

    @Schema(description = "유저 아이디", example = "abc@gmail.com", required = true)
    @NotNull(message = "이메일은 필 수 입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일형식에 맞춰주세요")
    private String userEmail;
    @Schema(description = "회원 이름")
    @NotNull(message = "이름은 필수입력입니다.")
    private String userName;

    @Schema(description = "비밀번호")
    @NotNull(message = "비밀번호는 필 수 입력입니다.")
    private String userPw;

    @Schema(description = "닉네임")
    @NotNull(message = "닉네임은 필 수 입력입니다.")
    private String nickName;

    @Schema(description = "회원 주소")
    @NotNull(message = "주소는 필 수 입력입니다.")
    private AddressDTO addressDTO;

    public static  MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = MemberDTO.builder()
                .userId(memberEntity.getUserId())
                .userEmail(memberEntity.getUserEmail())
                .userPw(memberEntity.getUserPw())
                .nickName(memberEntity.getNickName())
                .userName(memberEntity.getUserName())
                .addressDTO(AddressDTO.builder()
                        .userAddr(memberEntity.getAddress().getUserAddr())
                        .userAddrDetail(memberEntity.getAddress().getUserAddrDetails())
                        .userAddrEtc(memberEntity.getAddress().getUserAddrEtc())
                        .build())
                .build();

        return memberDTO;
    }
}
