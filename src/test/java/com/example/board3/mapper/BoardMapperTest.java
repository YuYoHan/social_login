package com.example.board3.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    @DisplayName("전체 조회 테스트")
    public void getListTest() {
        boardMapper.getList().forEach(boardDTO -> log.info(String.valueOf(boardDTO)));
    }

}