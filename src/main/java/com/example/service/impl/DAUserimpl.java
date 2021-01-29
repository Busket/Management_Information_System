package com.example.service.impl;

import com.example.entity.GPUser;
import com.example.repostitory.DAUserMapper;
import com.example.service.DAUserService;
import com.example.shiro.ShiroUtil;
import com.example.util.ActiveCodeUtils;
import com.example.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class DAUserimpl implements DAUserService {
    @Autowired
    private DAUserMapper daUserMapper;

    //根据邮箱查找用户
    @Override
    public GPUser selectUserByEmail(String email) {
        return daUserMapper.selectUserByEmail(email);
    }
    //查询所有用户
    @Override
    public PageInfo<GPUser> selectAllUser(int page, int limit) {
        return null;
    }

    //注册用户（添加用户）
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

        //此处设置activecode
        record.setActivecode(ActiveCodeUtils.giveCode());
        System.out.println(record.getActivecode());

        return daUserMapper.insertSelective(record);
    }
    //删除用户信息
    @Override
    public int delUserByID(List<String> ids) {
        return 0;
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
        return daUserMapper.updateToken(record);
    }
    //删除token(登出)
    @Override
    public int deleteLoginInfo(String email) {
        return daUserMapper.deletLoginInfo(email);
    }
    //根据邮件查找激活码验证是否正确
    @Override
    public int checkActiveCodebyEmail(String email, String activecode) {
        String dbactivecode=daUserMapper.checkActiveCodebyEmail(email);//获取数据库中的激活码
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
            daUserMapper.changeActiveCode(email,activecode);
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
        daUserMapper.changeActiveTime(record);
    }

    //忘记密码功能中的重新设置激活码，用于验证用户
    @Override
    public void resetActiveCode(String email) {
        //此处设置activecode
        String activeCode=ActiveCodeUtils.giveCode();
        System.out.println(email+" 忘记密码的激活码 "+activeCode);
        daUserMapper.changeActiveCode(email,activeCode);
    }

    //更改密码以及盐
    @Override
    public void updatePassword(GPUser record) {
        daUserMapper.updatePassword(record);
    }
}
