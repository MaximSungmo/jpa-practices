package me.kickscar.practices.jpa03.model10.repository;

import me.kickscar.practices.jpa03.model10.config.JpaConfig;
import me.kickscar.practices.jpa03.model10.domain.Genre;
import me.kickscar.practices.jpa03.model10.domain.Song;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaGenreRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaSongRepository songRepository;

    @Autowired
    private JpaGenreRepository genreRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        Song song1 = new Song();
        song1.setTitle("노래1");
        songRepository.save(song1);

        Song song2 = new Song();
        song2.setTitle("노래2");
        songRepository.save(song2);

        Genre genre1 = new Genre();
        genre1.setName("쟝르1");
        genre1.setAbbrName("쟝르1");
        genre1.addSong(song1);
        genre1.addSong(song2);
        genreRepository.save(genre1);

        Genre genre2 = new Genre();
        genre2.setName("쟝르2");
        genre2.setAbbrName("쟝르2");
        genre2.addSong(song1);
        genreRepository.save(genre2);

        Genre genre3 = new Genre();
        genre3.setName("쟝르3");
        genre3.setAbbrName("쟝르3");
        genreRepository.save(genre3);

        Genre genre4 = new Genre();
        genre4.setName("쟝르4");
        genre4.setAbbrName("쟝르4");
        genre4.addSong(song2);
        genreRepository.save(genre4);

        assertEquals(4L, genreRepository.count());
        assertEquals(2L, songRepository.count());
    }

    @Test
    @Transactional
    public void test02FindById() {
        Genre genre = genreRepository.findById(1L).get();

        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(genre.getSongs()));
        assertEquals(2L, genre.getSongs().size());
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(genre.getSongs()));
    }

    @Test
    public void test03FindById2() {
        Genre genre = genreRepository.findById2(1L);
        assertEquals(2L, genre.getSongs().size());
    }

    @Test
    @Transactional
    public void test04FindAll() {
        List<Genre> genres = genreRepository.findAll();

        assertEquals(4L, genres.size());
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(genres.get(0).getSongs()));
        assertEquals(2L, genres.get(0).getSongs().size());
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(genres.get(0).getSongs()));
    }

    @Test
    public void test05FindAll2() {
        List<Genre> grenres = genreRepository.findAll2();

        assertEquals(4L, grenres.size());
        assertEquals(2L, grenres.get(0).getSongs().size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test06RemoveSong(){
        Song song = songRepository.findById(1L).get();
        Genre genre = genreRepository.findById(1L).get();

        genre.removeSong(song);
    }
}