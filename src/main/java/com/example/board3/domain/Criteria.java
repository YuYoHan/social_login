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
    private final int PAGE_AMOUNT = 10;
    private int startRow;


    public Criteria(int pageNum) {
        this.pageNum = pageNum;
        // PAGE_AMOUNT를 10으로 정해놨으니
        // 10이 amount에 넣어집니다.
        // amount는 몇 페이지를 보여줄지 정해주는 것입니다.
        this.amount = PAGE_AMOUNT;
        this.startRow = (pageNum - 1) * PAGE_AMOUNT;
    }
}
