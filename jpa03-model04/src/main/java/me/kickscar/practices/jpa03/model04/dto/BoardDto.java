package me.kickscar.practices.jpa03.model04.dto;

import me.kickscar.practices.jpa03.model04.domain.Comment;

import java.util.Date;
import java.util.List;

public class BoardDto {
    private Long no;
    private Integer hit;
    private String title;
    private String contents;
    private Date regDate;
    private String userName;
    private List<CommentDto> comments;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
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

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "no=" + no +
                ", hit=" + hit +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", regDate=" + regDate +
                ", userName='" + userName + '\'' +
                ", comments=" + comments +
                '}';
    }
}
