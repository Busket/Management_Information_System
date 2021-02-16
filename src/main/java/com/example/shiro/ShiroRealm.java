package com.example.shiro;


import com.example.entity.GPUser;
import com.example.service.GPUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @version 1.0
 * @date 2020/5/30 11:07
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private GPUserService GPUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权开始了！");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证开始了！");
        //开始校验用户名和密码
        //取出令牌信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        System.out.println("验证用户是否存在");
        //登录验证分两个步骤：
        // 1、查询用户是否存在；
        //登录页面传来的用户名
        String email = token.getUsername();
        GPUser GPUser = GPUserService.selectUserByEmail(email);//数据库查找是否存在该邮箱，若存在则传出对象
        if(GPUser == null){
            System.out.println("用户不存在");
            return null;
        }
        System.out.println("用户存在！");
        //2、查询密码是否正确。
        String password = GPUser.getPassword();//获取 密码
        //Object principal, Object credentials,  ByteSource byteSource, String realmName
        String salt = GPUser.getSalt();//获取 盐
        ByteSource byteSource = ByteSource.Util.bytes(salt);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(GPUser,password, byteSource,getName());
        return simpleAuthenticationInfo;
    }
}
