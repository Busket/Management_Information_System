package com.example.service.impl;

import com.example.entity.GPUser;
import com.example.repository.GPUserMapper;
import com.example.service.GPUserService;
import com.example.shiro.ShiroUtil;
import com.example.util.ActiveCodeUtils;
import com.example.util.JwtUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class GPUserimpl implements GPUserService {
    @Autowired
    private GPUserMapper GPUserMapper;

    //根据邮箱查找用户
    @Override
    public GPUser selectUserByEmail(String email) {
        return GPUserMapper.selectUserByEmail(email);
    }
    //查询所有用户
    @Override
    public List<GPUser> selectAllUser(int page, int limit) {
        int row=(page-1)*limit;
        return GPUserMapper.selectAllUser(row,limit);//读图个参数用于确定行数，从哪开始拿，第二个确定数量
    }
    //查询用户总数量
    @Override
    public Integer selectUserCount() {
        return GPUserMapper.selectUserCount();//查询用户列表中有多少个
    }
    //关键次查询用户
    @Override
    public List<GPUser> selectAllUserByKeyword(String keywords, int curr, int pageSize) {
        return GPUserMapper.selectAllUserByKeyword(keywords+"%","%"+keywords+"%","%"+keywords);
    }

    //注册用户
    @Override
    public int insertSelective( @Param("record") GPUser record) {
        //此处密码做加盐加密
        String salt= UUID.randomUUID().toString();
        String message=record.getPassword();
        String encryption= ShiroUtil.encryptionBySalt(salt,message);
        //存储加密后的密码
        record.setPassword(encryption);
        record.setSalt(salt);//存储盐
        //获取timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        //此处设置creat_at
        record.setCreat_at(t);
        record.setUpdate_at(t);
        //如果是超级管理员添加用户
        if(record.getActivecode().equals("Actived")){
            record.setEmail_verified_at(t);
        }

        return GPUserMapper.insertSelective(record);
    }
    //删除用户信息
    @Override
    public int delUserByEmail(String email) {
        return GPUserMapper.delUserByEmail(email);
    }

    //修改用户的信息（可以不修改密码）
    @Override
    public int updateByPrimaryKeySelective(GPUser record) {
        return 0;
    }

    @Override
    public String generateJwtToken(GPUser GPUser) {
        //此处设置remember_token
        JwtUtil jwtUtil=new JwtUtil();
        String token=jwtUtil.createJWT(GPUser);//创建jwt
        System.out.println("token创建完成！");
        return token;
    }
    //更新token
    @Override
    public int updateToken(GPUser record) {
        return GPUserMapper.updateToken(record);
    }
    //删除token(登出)
    @Override
    public int deleteLoginInfo(String email) {
        return GPUserMapper.deletLoginInfo(email);
    }
    //根据邮件查找激活码验证是否正确
    @Override
    public int checkActiveCodebyEmail(String email, String activecode) {
        String dbactivecode= GPUserMapper.checkActiveCodebyEmail(email);//获取数据库中的激活码
        System.out.println(dbactivecode);
        if(dbactivecode.equals("Actived")){
            return -1;
        }else{
            if(activecode.equals(dbactivecode)){//进行激活码的比对
                return 1;
            }else{
                return 0;
            }
        }
    }
    //邮箱确认成功后，更改激活码
    @Override
    public int changeActiveCode(String email) {
        try{
            String activecode="Actived";//将激活码设置为Actived，以便登录时验证
            GPUserMapper.changeActiveCode(email,activecode);
            return 1;
        }catch (Exception e){
            return 0;
        }
    }
    //邮箱确认后添加确认时间，即修改Email_verified_at
    @Override
    public void changeActiveTime(String email) {
        Timestamp t = new Timestamp(System.currentTimeMillis());

        GPUser record=new GPUser();
        record.setEmail(email);
        record.setEmail_verified_at(t);
        GPUserMapper.changeActiveTime(record);
    }

    //忘记密码功能中的重新设置激活码，用于验证用户
    @Override
    public void resetActiveCode(String email) {
        //此处设置activecode
        String activeCode=ActiveCodeUtils.giveCode();
        System.out.println(email+" 忘记密码的激活码 "+activeCode);
        GPUserMapper.changeActiveCode(email,activeCode);
    }

    //更改密码以及盐
    @Override
    public void updatePassword(GPUser record) {
        GPUserMapper.updatePassword(record);
    }
    //根据用户邮箱更改用户信息
    @Override
    public int updateUserById(Integer id,String email, String name, String phone, Integer jurisdiction) {
        Timestamp update_at= new Timestamp(System.currentTimeMillis());



        return GPUserMapper.updateUserById(id,email,name,phone,jurisdiction,update_at);
    }
}
