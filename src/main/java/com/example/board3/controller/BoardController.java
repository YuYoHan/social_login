package com.example.board3.controller;

import com.example.board3.domain.BoardDTO;
import com.example.board3.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public ResponseEntity<List<BoardDTO>> list() {
        List<BoardDTO> list = boardService.getList();
        return ResponseEntity.ok().body(list);
    }
}
