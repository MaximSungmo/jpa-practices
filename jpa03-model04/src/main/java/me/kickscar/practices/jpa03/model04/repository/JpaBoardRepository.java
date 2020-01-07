package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBoardRepository extends JpaRepository<Board, Long>, JpaBoardQryDslRepository {
    List<Board> findAllByOrderByRegDateDesc();
}
