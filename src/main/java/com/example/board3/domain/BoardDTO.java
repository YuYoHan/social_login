package com.example.board3.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    @Schema(description = "게시판 번호", example = "1", required = true)
    private Long boardNum;
    @Schema(description = "게시판 작성자")
    private Long userId;
    @Schema(description = "게시판 작성 유저번호")
    private String boardTitle;
    @Schema(description = "해쉬테그")
    private String hashTag;
    @Schema(description = "게시판 이미지 경로")
    // 프론트에서 이미지를 s3에 올리면 서버에서는
    // 이미지 경로만 받아서 DB에 넣어준다.
    private List<String> boardImages;
    @Schema(description = "게시판 본문")
    private String boardContents;
    @Schema(description = "작성시간")
    private LocalDateTime regDate;
    @Schema(description = "수정시간")
    private LocalDateTime updateDate;
    @Schema(description = "게시판 제목")
    private String userEmail;

    @Builder

    public BoardDTO(Long boardNum,
                    Long userId,
                    String boardTitle,
                    String hashTag,
                    List<String> boardImages,
                    String boardContents,
                    LocalDateTime regDate,
                    LocalDateTime updateDate,
                    String userEmail) {
        this.boardNum = boardNum;
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.hashTag = hashTag;
        this.boardImages = boardImages;
        this.boardContents = boardContents;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.userEmail = userEmail;
    }
}
