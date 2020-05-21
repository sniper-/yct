package com.qdch.yct.web.entity;

/**
 * ClassName: User
 * Version:
 * Description:
 *
 * @Program:
 * @Author: sniper
 * @Date: 2020/05/21 14:52
 */
public class User {
    private Long id;
    private String userName;
    private String password;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
