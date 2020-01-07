package me.kickscar.practices.jpa03.model08.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name="name", nullable=false, length=200)
    private String name;

    @Column(name = "open_date", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date openDate = new Date();

    @MapsId
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", openDate=" + openDate +
                ", user=" + user +
                '}';
    }
}
