package me.kickscar.practices.jpa03.model10.domain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @Column(name = "no")
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_no"), inverseJoinColumns = @JoinColumn(name = "genre_no"))
    private Set<Genre> genres = new HashSet<>();

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre genre){
        genres.add(genre);
        genre.getSongs().add(this);
    }

    public void removeGenre(Genre genre){
        genres.remove(genre);
        genre.getSongs().remove(this);
    }



    @Override
    public String toString() {
        return "Song{" +
                "no=" + no +
                ", title='" + title + '\'' +
                // Test를 위해 주석처리(LAZY)
                // ", genres=" + genres +
                '}';
    }
}
