package com.example.board2.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String userEmail;
    private String userPassword;
    private String userPasswordCheck;
    private String userNickName;
    private String userPhoneNumber;
    private String userAddress;
    private String userAddressDetail;
}
