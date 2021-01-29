package com.example.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Getter
@Setter
public class GPUser {
    private int id;//用户id
    private String name;//用户名
    private String phone;//手机号
    private String email;//邮箱
    private String password;//密码
    //private String competence;//权限
    private Timestamp email_verified_at;//邮箱验证时间  email_varified_at
    private String remember_token;//token
    private Timestamp creat_at;//账号注册时间
    private Timestamp update_at;//账号更新时间
    private String salt;//盐
    private String activecode;//激活码，用于邮箱激活
    private int jurisdiction;//权限

    public int getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(int jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    //头像...
    //逻辑删除（注销）
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public String getCompetence() {
//        return competence;
//    }
//
//    public void setCompetence(String competence) {
//        this.competence = competence;
//    }

    public Timestamp getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(Timestamp email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public Timestamp getCreat_at() {
        return creat_at;
    }

    public void setCreat_at(Timestamp creat_at) {
        this.creat_at = creat_at;
    }

    public Timestamp getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Timestamp update_at) {
        this.update_at = update_at;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getActivecode() {
        return activecode;
    }

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }


}
