package me.kickscar.practices.jpa03.model09.repository;

import me.kickscar.practices.jpa03.model09.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGenreRepository extends JpaRepository<Genre, Long>{
    Genre findByName(String name);
}
