package com.example.board3.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@ToString
@Setter
public class Criteria {

    private int pageNum;
    private int amount;
    private String type;
    private String keyword;
    private int startRow;

    public Criteria() {
        this(1, 10);
    }

    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }

    public String[] getTypeArr() {
        return type == null ? new String[] {} : type.split("");
    }

    public String getListLink() {
        // ? 앞에 오는 uri 문자열
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                // 파라미터 추가
                .queryParam("pageNum", pageNum)
                .queryParam("amount", amount)
                .queryParam("keyword", keyword)
                .queryParam("type", type);

        // ?pageNum=3&amount=10&keyword=app&type=TC
        // 빌더가 가지고 있는 설정대로 문자열 만들기
        return builder.toUriString();
    }
}
