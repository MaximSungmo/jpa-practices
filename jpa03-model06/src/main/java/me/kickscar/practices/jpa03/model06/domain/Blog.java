package me.kickscar.practices.jpa03.model06.domain;


import javax.persistence.*;

@Entity
@Table(name="blog")
public class Blog {
    @Id
    @Column(name="no")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long no;

    @Column(name="name", nullable=false, length=200)
    private String name;

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

    @Override
    public String toString() {
        return "Blog{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}
