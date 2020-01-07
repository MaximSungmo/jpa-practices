package me.kickscar.practices.jpa03.model09.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @Column(name = "no")
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ManyToMany
    @JoinTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_no"), inverseJoinColumns = @JoinColumn(name = "genre_no"))
    private List<Genre> genres = new ArrayList<Genre>();

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

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
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
