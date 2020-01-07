package me.kickscar.practices.jpa03.model08.dto;

import com.querydsl.core.annotations.QueryProjection;

public class BlogDto {
    private String name;
    private String userId;
    private String userName;

    public BlogDto(){
    }

    @QueryProjection
    public BlogDto(String name, String userId, String userName){
        this.name = name;
        this.userId = userId;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "BlogDto{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
