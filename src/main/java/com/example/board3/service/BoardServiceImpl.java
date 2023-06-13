package com.example.board3.service;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.Criteria;
import com.example.board3.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getList() {
        return boardMapper.getList();
    }

    @Override
    public void write(BoardDTO boardDTO) {
        boardMapper.insert(boardDTO);
    }

    @Override
    public BoardDTO get(Long boardNum) {
        return boardMapper.getDetail(boardNum);
    }

    @Override
    public void modify(BoardDTO board) {
        boardMapper.update(board);
    }

    @Override
    public void remove(Long boardNum) {
        boardMapper.delete(boardNum);
    }

    @Override
    public int count(Criteria cri) {
        return boardMapper.getTotal(cri);
    }
}
