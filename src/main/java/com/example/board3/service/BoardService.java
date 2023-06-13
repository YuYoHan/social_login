package com.example.board3.service;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.Criteria;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getList();
    void write(BoardDTO boardDTO);
    BoardDTO get(Long boardNum);
    void modify(BoardDTO board);
    void remove(Long boardNum);
    int count(Criteria cri);
}
