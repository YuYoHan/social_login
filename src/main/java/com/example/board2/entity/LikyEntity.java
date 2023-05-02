package com.example.board2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity(name = "Liky")
@Table(name = "liky")
@AllArgsConstructor
@NoArgsConstructor
public class LikyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeId;
    private int boardNumber;
    private String userEmail;
    private String likeUserProfile;
    private String likeUserNickName;
}
