package me.kickscar.practices.jpa03.model04.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "comment" )
public class Comment {

    @Id
    @Column(name = "no")
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;

    @Column( name = "contents", nullable = false )
    @Lob
    private String contents;

    @Column( name = "reg_date", nullable = false )
    @Temporal( value = TemporalType.TIMESTAMP )
    private Date regDate = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "user_no" )
    private User user;

    public Comment(){
    }

    public Comment(User user, String contents){
        this.user = user;
        this.contents = contents;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "no=" + no +
                ", contents='" + contents + '\'' +
                ", regDate=" + regDate +
                ", user=" + user +
                '}';
    }
}