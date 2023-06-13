package com.example.board3.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageDTO {
    private int startPage;
    private int endPage;
    private int realEnd;
    private int total;
    private boolean prev, next;
    private Criteria cri;

    public PageDTO(int total, Criteria cri) {
        int pageNum = cri.getPageNum();
        this.total = total;
        this.cri = cri;

        this.endPage = (int)Math.ceil(pageNum/10.0) * 10;
        this.startPage = this.endPage - (cri.getAmount() -1);
        this.realEnd = (int)Math.ceil(total*1.0/10);

        endPage = endPage > realEnd ? realEnd : endPage;

        this.prev = this.startPage > 1;
        this.next = this.endPage < this.realEnd;
    }
}
