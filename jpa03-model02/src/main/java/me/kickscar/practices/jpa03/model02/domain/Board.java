package me.kickscar.practices.jpa03.model02.domain;

import javax.persistence.*;
import java.util.Date;

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

    @Transient // 실제 게시판이 아니기 때문에,,, (계층형 구현 컬럼은 오밋)
    @Column( name = "group_no", nullable = false )
	private Integer groupNo;

    @Transient // 실제 게시판이 아니기 때문에,,, (계층형 구현 컬럼은 오밋)
    @Column( name = "order_no", nullable = false )
	private Integer orderNo;

    @Transient // 실제 게시판이 아니기 때문에,,, (계층형 구현 컬럼은 오밋)
    @Column( name = "depth", nullable = false )
	private Integer depth;

    @ManyToOne(fetch = FetchType.EAGER)   // @ManytoOne, @OneToOne에서 Default는 FetchType.EAGER
    @JoinColumn( name = "user_no" )
    private User user;

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

    @Override
    public String toString() {
        return "Board{" +
                "no=" + no +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", hit=" + hit +
                ", regDate='" + regDate + '\'' +
                ", user=" + user +
                '}';
    }
}