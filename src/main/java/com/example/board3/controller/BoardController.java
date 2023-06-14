package com.example.board3.controller;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.Criteria;
import com.example.board3.domain.PageDTO;
import com.example.board3.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "board", description = "게시판 API")
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 조회
    @GetMapping("/{page}")
    @Tag(name = "board")
    @Operation(summary = "게시글 보기", description = "전체 게시글을 볼 수 있습니다.")
    public ResponseEntity<?> list(@PathVariable int page) {

        // page를 PathVariable로 받아오면 그것을 page에 넣어줍니다.
        // Criteria 클래스로 page를 넘겨줍니다.
        Criteria cri = new Criteria(page);
        // 페이지를 끊어서 보여주기 위해서 전체 보여주는 메소드에 page를 넘겨줍니다.
        // 10개만 보여주기로 했으면 10개만 나옵니다.
        List<BoardDTO> boardList = boardService.getList(cri);

        // PageDTO에 SQL에서 count로 나온 int, 즉 total과 cri를 보내준다.
        PageDTO pageDTO = new PageDTO(boardService.getBoardNum(), cri);

        Map<String, Object> map = new HashMap<>();

        map.put("boardList", boardList);
        map.put("pageDTO", pageDTO);

        log.info("모든 게시글 보기: {}", boardList);

        return ResponseEntity.ok().body(map);
    }

    // 게시글 상세 정보
    @GetMapping("/{boardNum}")
    @Tag(name = "board")
    @Operation(summary = "게시글 보기", description = "게시글의 상세 정보를 볼 수 있습니다.")
    public ResponseEntity<BoardDTO> get(@PathVariable Long boardNum) {
        try {
            BoardDTO boardDTO = boardService.get(boardNum);
            if(boardDTO != null) {
                log.info("board : " + boardDTO);
                return ResponseEntity.ok().body(boardDTO);
            } else {
                log.info("해당 게시글이 없습니다.");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 게시글 등록
    @PostMapping("/")
    @Tag(name = "board")
    @Operation(summary = "게시글 등록", description = "게시글을 입력하는 API")
    public ResponseEntity<?> write(@RequestBody BoardDTO boardDTO) {

        BoardDTO board = BoardDTO.builder()
                .userId(boardDTO.getUserId())
                .boardTitle(boardDTO.getBoardTitle())
                .boardContents(boardDTO.getBoardContents())
                .hashTag(boardDTO.getHashTag())
                .boardImages(boardDTO.getBoardImages())
                .userEmail(boardDTO.getUserEmail())
                .build();

        try {
            boardService.write(board);
            return ResponseEntity.ok().body("게시글 작성 완료했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 게시글 수정
    @PutMapping("/")
    @Tag(name = "board")
    @Operation(summary = "게시글 수정", description = "게시글을 수정하는 API")
    public ResponseEntity<?> modify(@RequestBody BoardDTO boardDTO) {
        boardService.modify(boardDTO);
        return ResponseEntity.ok().body(boardDTO);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardNum}")
    public ResponseEntity<?> removeUser(@PathVariable Long boardNum) {
        boardService.remove(boardNum);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
