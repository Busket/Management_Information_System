package com.example.service;

import com.example.entity.GPUser;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GPUserService {
    //根据邮箱查找用户
    GPUser selectUserByEmail(String email);

    //查询所有用户
    List<GPUser> selectAllUser(int page, int limit);

    //查询所有用户
    Integer selectUserCount();

    //关键字查询
    List<GPUser> selectAllUserByKeyword(String keywords, int curr, int pageSize);

    //注册用户（添加用户）
    int insertSelective(GPUser record);

    //删除用户信息
    int delUserByID( @Param("ids") List<String> ids);

    //修改用户的信息（可以不修改密码）
    int updateByPrimaryKeySelective(GPUser record);
    //获得token
    String generateJwtToken(GPUser GPUser);
    //数据库更新token
    int updateToken(GPUser record);
    //登出，删除token
    int deleteLoginInfo(String email);
    //邮箱确认，通过邮箱查找激活码，再进行比对
    int checkActiveCodebyEmail(String email, String activecode);
    //邮箱确认成功后，更改激活码
    int changeActiveCode(String emai);
    //邮箱确认后添加确认时间，即修改Email_verified_at
    void changeActiveTime(String email);
    //忘记密码时重置激活码
    void resetActiveCode(String email);
    //更改密码以及盐
    void updatePassword(GPUser record);

}
