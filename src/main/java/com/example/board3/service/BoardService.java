package com.example.board3.service;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.BoardImageDTO;
import com.example.board3.domain.Criteria;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getList(Criteria cri);
    int getBoardNum();
    void write(BoardDTO boardDTO);
    BoardDTO get(Long boardNum);
    void modify(BoardDTO board);
    void remove(Long boardNum);
    int count(Criteria cri);
    // 이미지 찾기
    List<String> findBoardImagesByBoardNum(Long boardNum);
}
