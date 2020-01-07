package me.kickscar.practices.jpa03.model09.repository;

import me.kickscar.practices.jpa03.model09.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSongRepository extends JpaRepository<Song, Long>, JpaSongQryDslRepository {
}
