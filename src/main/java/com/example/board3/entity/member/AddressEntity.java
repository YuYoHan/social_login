package com.example.board3.entity.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@ToString
@Getter
@NoArgsConstructor
public class AddressEntity {
    private String userAddr;
    private String userAddrDetails;
    private String userAddrEtc;

    @Builder
    public AddressEntity(String userAddr, String userAddrDetails, String userAddrEtc) {
        this.userAddr = userAddr;
        this.userAddrDetails = userAddrDetails;
        this.userAddrEtc = userAddrEtc;
    }
}
