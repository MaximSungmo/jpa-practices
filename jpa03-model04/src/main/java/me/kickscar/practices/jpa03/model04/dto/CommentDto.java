package me.kickscar.practices.jpa03.model04.dto;

import java.util.Date;

public class CommentDto {
    private Long no;
    private String contents;
    private Date regDate;
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "no=" + no +
                ", contents='" + contents + '\'' +
                ", regDate=" + regDate +
                ", userName='" + userName + '\'' +
                '}';
    }
}
