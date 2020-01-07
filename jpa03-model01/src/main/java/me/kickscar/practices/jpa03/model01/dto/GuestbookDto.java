package me.kickscar.practices.jpa03.model01.dto;

import java.util.Date;

public class GuestbookDto {
    private Long no;
    private String name;
    private String contents;
    private Date regDate;

    public GuestbookDto(Long no, String name, String contents, Date regDate) {
        this.no = no;
        this.name = name;
        this.contents = contents;
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "GuestbookDto{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", regDate=" + regDate +
                '}';
    }
}
