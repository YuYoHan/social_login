package com.example.board2.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "comment")
@Entity(name = "Comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;
    private int boardNumber;
    private String userEmail;
    private String commentUserProfile;
    private String commentUserNickName;
    private String commentWriteDate;
    private String commentContent;
}
