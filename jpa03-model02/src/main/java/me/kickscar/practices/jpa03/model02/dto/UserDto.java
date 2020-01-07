package me.kickscar.practices.jpa03.model02.dto;

public class UserDto {
    private Long no;
    private String name;

    public UserDto(Long no, String name) {
        this.no = no;
        this.name = name;
    }

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
        return "UserDto{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}
