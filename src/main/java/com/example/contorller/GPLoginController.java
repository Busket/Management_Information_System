package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPUser;
import com.example.service.GPUserService;

import com.example.service.MailService;
import com.example.shiro.ShiroUtil;
//import jdk.nashorn.internal.runtime.Context;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@Controller
public class GPLoginController {
    @Autowired
    private GPUserService GPUserService;
    @Autowired
    MailService mailService;//用于发送确认邮件


    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<HashMap<String, Object>> login(String email, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(email);
        System.out.println(password);
        try {
            //将用户请求参数封装后，直接提交给Shiro处理
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);
            //Shiro认证通过后会将user信息放到subject内，生成token并返回
            GPUser GPUser = (GPUser) subject.getPrincipal();
            if (GPUser.getActivecode().equals("Actived")) {//验证用户是否被激活了
                String newToken = GPUserService.generateJwtToken(GPUser);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", String.valueOf(GPUser.getId()));
                map.put("userName", GPUser.getName());
                map.put("userPhone", GPUser.getPhone());
                map.put("userEmail", GPUser.getEmail());
                map.put("userToken", GPUser.getRemember_token());
                map.put("userJurisdiction", String.valueOf(GPUser.getJurisdiction()));
                jsonObject.put("status", "SUCCESS");
                jsonObject.put("userInfo", map);
                writer.write(jsonObject.toJSONString());
                writer.close();

                //对token进行持久化
                GPUser record = new GPUser();
                record.setEmail(email);
                record.setRemember_token(newToken);
                GPUserService.updateToken(record);

                System.out.println("验证成功，返回token");
                return ResponseEntity.ok().build();
            } else {
                //邮箱未验证
                jsonObject.put("message", "邮箱未验证！该用户尚未激活！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                System.out.println("用户尚未激活！");
                return ResponseEntity.ok().build();
            }

        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            jsonObject.put("message", "账户或者密码不正确！请查证后再次登录！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("User " + email + " login fail, Reason:" + e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return ResponseEntity.ok().build();
        }
    }


    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            GPUser GPUser = (GPUser) subject.getPrincipals().getPrimaryPrincipal();
            GPUserService.deleteLoginInfo(GPUser.getEmail());//根据email删除token
            System.out.println("用户登出成功！");
        }
        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }


    //忘记密码功能
    @RequestMapping(value = "/forgetPassword")
    public Object forgetPassword(String email, String phone, HttpServletRequest request, HttpServletResponse response) throws MessagingException, IOException {
        GPUser GPUser = GPUserService.selectUserByEmail(email);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        if (GPUser == null) {//如果在数据库中找不到该用户
            jsonObject.put("message", "找不到该用户！！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        } else if (GPUser.getPhone().equals(phone)) {//对证手机号码是否正确
            //进入验证阶段

            /*这里考虑要不要添加邮箱激活验证？*/

            //设置激活码
            GPUserService.resetActiveCode(email);
            GPUser = GPUserService.selectUserByEmail(email);//重新赋值
            mailService.sendMimeMail(GPUser.getEmail(), "畅途驾校欢迎您使用我们的系统！", "您的账号：。\n邮箱：" + GPUser.getEmail() + " 正在修改登录密码，如有问题请联系管理员\n解锁码：" + GPUser.getActivecode());
            jsonObject.put("status", "Success");
            jsonObject.put("message", "已找到该用户，允许密码修改！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();//"重置密码邮件以发送，转跳至填写页面resetpassword（激活码，密码）"
        } else {
            jsonObject.put("message", "邮箱与联系方式不匹配！！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    //忘记密码的重新设置功能
    @RequestMapping(value = "/resetPassword")
    public Object resetPassword(String email, String activecode, String password, HttpServletRequest request, HttpServletResponse response) {
//        DAUser daUser=new DAUser();
//        daUser.setEmail(request.getHeader("email"));
//        daUser.setActivecode(activecode);
        GPUser GPUser = GPUserService.selectUserByEmail(email);
        if (GPUser == null) return "邮箱输入错误或者距离注册已经超过24小时";//如果在数据库中找不到该用户
        if (GPUser.getActivecode().equals(activecode)) {
            //此处密码做加盐加密
            String salt = UUID.randomUUID().toString();
            String message = password;
            String encryption = ShiroUtil.encryptionBySalt(salt, message);
            //存储加密后的密码
            GPUser.setPassword(encryption);
            GPUser.setSalt(salt);//存储盐

            GPUserService.updatePassword(GPUser);//对密码和盐进行存储
            GPUserService.changeActiveCode(email);//将激活码改回Actived
            System.out.println("密码修改成功");
            return "密码修改成功";
        } else {
            System.out.println("激活码不正确");
            return "激活码不正确";
        }
    }


}
