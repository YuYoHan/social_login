package com.example.board3.service;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.BoardImageDTO;
import com.example.board3.domain.Criteria;
import com.example.board3.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;

    @Override
    public List<BoardDTO> getList(Criteria cri) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        List<BoardDTO> returnBoardList = new ArrayList<>();

        int boardCount = boardMapper.getBoardCount();

        if(boardCount > 0) {
            boardDTOList = boardMapper.getList(cri);
        }

        for (BoardDTO board: boardDTOList
             ) {
            Long boardNum = board.getBoardNum();
            List<String> boardImages = findBoardImagesByBoardNum(boardNum);
            Long userId = board.getUserId();
            String userEmail = board.getUserEmail();

            BoardDTO board2 = BoardDTO.builder()
                    .boardNum(boardNum)
                    .regDate(board.getRegDate())
                    .userId(userId)
                    .userEmail(userEmail)
                    .boardImages(boardImages)
                    .boardTitle(board.getBoardTitle())
                    .boardContents(board.getBoardContents())
                    .hashTag(board.getHashTag())
                    .build();

            returnBoardList.add(board2);
        }

        return returnBoardList;
    }

    @Override
    public void write(BoardDTO boardDTO) {

        List<String> boardImages = boardDTO.getBoardImages();
        boardMapper.insert(boardDTO);

        if(boardImages.size() != 0) {
            Long lastBoardNum = boardMapper.getLastBoardNum();
            boardMapper.insertBoardImage(lastBoardNum, boardImages);
        }
    }

    @Override
    public BoardDTO get(Long boardNum) {
        return boardMapper.getDetail(boardNum);
    }

    @Override
    public void modify(BoardDTO board) {
        boardMapper.update(board);
        Long boardNum = board.getBoardNum();
        boardMapper.deleteBoardImage(boardNum);

        if(board.getBoardImages().size() != 0) {
            List<String> boardImages = board.getBoardImages();
            boardMapper.insertBoardImage(boardNum, boardImages);
        }
    }

    @Override
    public void remove(Long boardNum) {
        boardMapper.delete(boardNum);
    }

    @Override
    public int count(Criteria cri) {
        return boardMapper.getTotal(cri);
    }

    @Override
    public int getBoardNum() {
        return boardMapper.getBoardCount();
    }

    @Override
    public List<String> findBoardImagesByBoardNum(Long boardNum) {
        List<String> boardImages = new ArrayList<>();
        List<BoardImageDTO> boardImagesList = boardMapper.findBoardImagesByBoardNum(boardNum);

        for (BoardImageDTO boardImage : boardImagesList) {
            // log.info(boardImage.getBoardImg());
            boardImages.add(boardImage.getBoardImg());
        }

        return boardImages;
    }
}
