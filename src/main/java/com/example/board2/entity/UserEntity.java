package com.example.board2.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "User")
@Table(name = "user")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    private String userEmail;
    private String userPassword;
    private String userNickName;
    private String userPhoneNumber;
    private String userAddress;
    private String userProfile;
}
