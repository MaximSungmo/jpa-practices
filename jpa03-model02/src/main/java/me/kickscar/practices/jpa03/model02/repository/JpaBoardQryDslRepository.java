package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.dto.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JpaBoardQryDslRepository {
    public BoardDto findById2(Long no);

    public List<Board> findAllByOrderByRegDateDesc2();

    public List<BoardDto> findAllByOrderByRegDateDesc3();
    public List<BoardDto> findAllByOrderByRegDateDesc3(Integer page, Integer size);
    public List<BoardDto> findAll3(Pageable pageable);
    public List<BoardDto> findAll3(String keyword, Pageable pageable);

    public Boolean update(Board board);

    public Boolean delete(Long no);
    public Boolean delete(Long boardNo, Long userNo);
}
