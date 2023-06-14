package com.example.board3.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PageDTO {
    private int startPage;
    private int endPage;
    private int realEnd;
    private int total;
    private boolean prev, next;

    public PageDTO(int total, Criteria cri) {
        // pageNum에 cri로 넘어온 page를 넣어줍니다.
        // 즉, pathVariable로 5페이지가 넘어왔으면 여기에도 Criteria를 거쳐서
        // 5가 넘어옵니다.
        int pageNum = cri.getPageNum();
        this.total = total;

        this.endPage = (int)Math.ceil(pageNum/10.0) * 10;
        this.startPage = this.endPage - (cri.getAmount() -1);
        this.realEnd = (int)Math.ceil(total*1.0/10);

        // endPage가 realEnd보다 크면 realEnd를 반환하고 작으면 endPage를 반환
        endPage = endPage > realEnd ? realEnd : endPage;

        // 화살표(< >)이 역할이다.
        // startPage가 1보다 크면 이전이 있으니 < 마크가 뜬다.
        this.prev = this.startPage > 1;
        // endPage가 realEnd보다 작다면 > 마크가 나온다.
        this.next = this.endPage < this.realEnd;
    }
}
