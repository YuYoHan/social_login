package com.example.social.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;

// 임베디드 타입을 사용하려면 넣어야한다.
// 이렇게 따로 빼는 이유는 주소는 여러개로 나누어져 있는데
// Member 안에 전부 추가하면 길어지기 때문에 따로 빼놓는다.
@Embeddable
@ToString
@Getter
@NoArgsConstructor
public class AddressEntity {
    private String userAddr;
    private String userAddrDetail;
    private String userAddrEtc;

    @Builder
    public AddressEntity(String userAddr, String userAddrDetail, String userAddrEtc) {
        this.userAddr = userAddr;
        this.userAddrDetail = userAddrDetail;
        this.userAddrEtc = userAddrEtc;
    }
}
