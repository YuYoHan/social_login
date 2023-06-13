package com.example.board3.mapper;

import com.example.board3.domain.BoardDTO;
import com.example.board3.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 이 방법은 mapper에서 직접 query문을 작성할 때 사용하는 방법이다.
    // SQL을 작성할 때 ;이게 들어가면 안된다.
//    @Select("select * from board where boardNum > 0")
    List<BoardDTO> getList();
    BoardDTO read(Long boardNum);

    void insert(BoardDTO boardDTO);

    BoardDTO getDetail(Long boardNum);
    int getTotal(Criteria cri);
    void update(BoardDTO boardDTO);
    void delete(Long boardNum);
}
