package com.example.social.domain;

import com.example.social.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long userId;

    @NotNull(message = "이메일은 필수 입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String userEmail;

    @NotNull(message = "이름은 필수입력입니다.")
    private String userName;

    @NotNull(message = "닉네임은 필수입력입니다.")
    private String nickName;

    private String userPw;

    @NotNull(message = "유저 타입은 필 수 입니다.")
    private Role role;

    private String provider;        // 예) google

    private String providerId;

    private AddressDTO addressDTO;

    @Builder
    public MemberDTO(Long userId,
                     String userEmail,
                     String userName,
                     String nickName,
                     String userPw,
                     Role role,
                     String provider,
                     String providerId,
                     AddressDTO addressDTO) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.nickName = nickName;
        this.userPw = userPw;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.addressDTO = addressDTO;
    }

    public static MemberDTO toMemberDTO(MemberEntity member) {
        return MemberDTO.builder()
                .userId(member.getUserId())
                .userEmail(member.getUserEmail())
                .userName(member.getUserName())
                .userPw(member.getUserPw())
                .nickName(member.getNickName())
                .role(member.getRole())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .addressDTO(AddressDTO.builder()
                        .userAddr(member.getAddress().getUserAddr())
                        .userAddrDetails(member.getAddress().getUserAddrDetail())
                        .userAddrEtc(member.getAddress().getUserAddrEtc())
                        .build())
                .build();
    }
}
