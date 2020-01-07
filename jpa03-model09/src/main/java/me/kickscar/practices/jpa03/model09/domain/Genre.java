package me.kickscar.practices.jpa03.model09.domain;

import javax.persistence.*;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "abbr_name", nullable = false, length = 5)
    private String abbrName;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", abbrName='" + abbrName + '\'' +
                '}';
    }
}
