package com.example.board2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "PopularSearch")
@Table(name = "popularSearch")
public class PopularSearchEntity {
    @Id
    private String popularTerm;
    private int popularSearchCount;
}
