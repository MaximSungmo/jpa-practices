package me.kickscar.practices.jpa03.model10.repository;

import me.kickscar.practices.jpa03.model10.domain.Genre;

import java.util.List;

public interface JpaGenreQryDslRepository {
    Genre findById2(Long no);
    List<Genre> findAll2();
}
