package com.example.board3.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class AddressDTO {
    @Schema(description = "우편번호", required = true)
    private String userAddr;
    @Schema(description = "주소", required = true)
    private String userAddrDetail;
    @Schema(description = "상세 주소", required = true)
    private String userAddrEtc;

    @Builder
    public AddressDTO(String userAddr, String userAddrDetail, String userAddrEtc) {
        this.userAddr = userAddr;
        this.userAddrDetail = userAddrDetail;
        this.userAddrEtc = userAddrEtc;
    }
}
