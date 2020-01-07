package me.kickscar.practices.jpa03.model02.dto;

import java.util.Date;

public class BoardDto {
    private Long no;
    private Integer hit;
    private String title;
    private String contents;
    private Date regDate;
    private String userName;

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

    @Override
    public String toString() {
        return "BoardDto{" +
                "no=" + no +
                ", hit=" + hit +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", regDate=" + regDate +
                ", userName='" + userName + '\'' +
                '}';
    }
}
