package me.kickscar.practices.jpa03.model10.repository;

import me.kickscar.practices.jpa03.model10.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGenreRepository extends JpaRepository<Genre, Long>, JpaGenreQryDslRepository  {
}
