package com.example.shiro;


import com.example.util.ConstantUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @version 1.0
 * @date 2020/5/30 11:07
 */
@Configuration
public class ShiroConfig {
    //realm
    @Bean
    public ShiroRealm shiroRealm(){
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
        return shiroRealm;
    }

    //securityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(shiroRealm());
        return defaultWebSecurityManager;
    }

    //shiroFilterFactorybean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/login","anon");//设置放行界面
        map.put("/register","anon");
        //map.put("/*","authc");//必须认证，设置拦截界面（除了上面放行的，其他都会被拦截）
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        shiroFilterFactoryBean.setLoginUrl("/toLogin");//   /toLogin
        return shiroFilterFactoryBean;
    }

    //实例化密码比较器
    @Bean
    public CredentialsMatcher credentialsMatcher(){
        CredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //使用MD5加密
        ((HashedCredentialsMatcher) credentialsMatcher).setHashAlgorithmName(ConstantUtils.algorithmName);
        //加密次数为1000次
        ((HashedCredentialsMatcher) credentialsMatcher).setHashIterations(ConstantUtils.hashIterations);
        return  credentialsMatcher;
    }
}
