package com.example.board3.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long boardNum;
    private String boardTitle;
    private String boardContents;
    private String userId;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    @Builder
    public BoardDTO(String boardTitle, String boardContents, String userId, LocalDateTime regDate, LocalDateTime updateDate) {
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.userId = userId;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }
}
