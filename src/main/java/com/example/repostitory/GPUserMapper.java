package com.example.repostitory;

import com.example.entity.GPUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GPUserMapper {

    //根据邮箱查找用户
    GPUser selectUserByEmail(String email);

    //保存添加的用户
    int insertSelective(GPUser record);

    //保存（更新）token
    int updateToken(GPUser record);

    //登出
    int deletLoginInfo(String email);

    //邮件确认，通过邮箱查找激活码
    String checkActiveCodebyEmail(String email);

    //邮箱确认成功后，更改激活码
    void changeActiveCode(String email, String activecode);

    //邮箱确认后，更改邮箱确认时间
    void changeActiveTime(GPUser record);

    //更改密码以及盐
    void updatePassword(GPUser record);

    //查找所有用户
    List<GPUser> selectAllUser(int row,int limit);

    //查看用户数量
    Integer selectUserCount();

//    int deleteByPrimaryKey(Integer userId);
//
//    int insert(DAUser record);
//

//
//    TUser selectByPrimaryKey(Integer userId);
//
//    int updateByPrimaryKeySelective(TUser record);//修改用户的信息（可以不修改密码）
//
//    int updateByPrimaryKey(TUser record);
//
//    List<TUser> selectAllUser();//查询所有用户
//
//    int delUserByID( @Param("ids") List<String> ids); //删除选中的用户
//
//    TUser selectUserByName(String name);

}
