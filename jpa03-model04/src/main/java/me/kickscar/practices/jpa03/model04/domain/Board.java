package me.kickscar.practices.jpa03.model04.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "board" )
public class Board {

    @Id
    @Column( name = "no" )
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;

    @Column( name = "title", nullable = false, length = 200 )
	private String title;

    @Column( name = "contents", nullable = false )
    @Lob
	private String contents;

    @Column( name = "hit", nullable = false )
    private Integer hit;

    @Column( name = "reg_date", nullable = false )
    @Temporal( value = TemporalType.TIMESTAMP )
    private Date regDate = new Date();

    @Transient
    @Column( name = "group_no", nullable = false )
	private Integer groupNo;

    @Transient
    @Column( name = "order_no", nullable = false )
	private Integer orderNo;

    @Transient
    @Column( name = "depth", nullable = false )
	private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_no" )
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_no")
    private List<Comment> comments = new ArrayList<Comment>();

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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Board{" +
                "no=" + no +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", hit=" + hit +
                ", regDate=" + regDate +
                ", groupNo=" + groupNo +
                ", orderNo=" + orderNo +
                ", depth=" + depth +
                // test를 위해 주석 처리
                // ", user=" + user +
                // ", comments=" + comments +
                '}';
    }
}