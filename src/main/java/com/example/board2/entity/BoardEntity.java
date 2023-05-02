package com.example.board2.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@Entity(name = "Board")
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNumber;
    private String boardTitle;
    private String boardContent;
    private String boardImage;
    private String boardVideo;
    private String boardFile;
    private String boardWriterEmail;
    private String boardWriterProfile;
    private String boardWriterNickName;
    private String boardWriteDate;
    private int boardClickCount;
    private int boardLikeCount;
    private int boardCommentCount;
}
