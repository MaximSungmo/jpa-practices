package me.kickscar.practices.jpa03.model10.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model10.domain.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model10.domain.QGenre.genre;
import static me.kickscar.practices.jpa03.model10.domain.QSong.song;


public class JpaGenreQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaGenreQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaGenreQryDslRepositoryImpl() {
        super(Genre.class);
    }

    @Override
    public Genre findById2(Long no) {
        return queryFactory
                .selectDistinct(genre)
                .from(genre)
                .leftJoin(genre.songs, song)
                .fetchJoin()
                .where(genre.no.eq(no))
                .fetchOne();
    }

    @Override
    public List<Genre> findAll2() {
        return queryFactory
                .selectDistinct(genre)
                .from(genre)
                .leftJoin(genre.songs, song)
                .fetchJoin()
                .fetch();
    }
}