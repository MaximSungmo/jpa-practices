package me.kickscar.practices.jpa03.model09.repository;

import me.kickscar.practices.jpa03.model09.domain.Song;

import java.util.List;

public interface JpaSongQryDslRepository {
    Song findById2(Long no);
    List<Song> findAll2();
    void deleteGenreByIdAndGenreId(Long songNo, Long genreNo);
}
