package com.example.board3.mapper;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.BoardImageDTO;
import com.example.board3.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    // Criteria에서 정해준 갯수에 맞는 게시글 보여줌
    List<BoardDTO> getList(Criteria cri);

    // 게시글 전체 갯수(total) 가져오기 위한 것
    int getBoardCount();

    // 게시글 상세 조회
    BoardDTO read(Long boardNum);

    // 게시글 작성
    void insert(BoardDTO boardDTO);

    // 게시글 상세 조회
    BoardDTO getDetail(Long boardNum);

    // 게시글 토탈
    int getTotal(Criteria cri);

    // 게시글 수정
    void update(BoardDTO boardDTO);

    // 게시글 삭제
    void delete(Long boardNum);

    // 마지막 게시글 번호 가져옴
    Long getLastBoardNum();

    // 이미지 넣기
    void insertBoardImage(@Param("boardNum") Long boardNum,
                          @Param("boardImages") List<String > boardImages);

    // 이미지 찾기
    List<BoardImageDTO> findBoardImagesByBoardNum(Long boardNum);

    // 이미지 삭제
    void deleteBoardImage(Long boardNum);
}
