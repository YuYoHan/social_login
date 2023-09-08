package com.example.social.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AddressDTO {
    private String userAddr;
    private String userAddrDetails;
    private String userAddrEtc;

    @Builder
    public AddressDTO(String userAddr, String userAddrDetails, String userAddrEtc) {
        this.userAddr = userAddr;
        this.userAddrDetails = userAddrDetails;
        this.userAddrEtc = userAddrEtc;
    }
}
