package com.example.contorller;

import com.alibaba.fastjson.JSON;
import com.example.entity.GPUser;
import com.example.service.DAUserService;

import com.example.service.MailService;
import com.example.shiro.ShiroUtil;
//import jdk.nashorn.internal.runtime.Context;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;


@Controller
public class GPLoginController {
    @Autowired
    private DAUserService daUserService;
    @Autowired
    MailService mailService;//用于发送确认邮件


    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/login")
    public ResponseEntity<Void> login(String email, String password, HttpServletRequest request, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        System.out.println(email);
        System.out.println(password);
        try {
            //将用户请求参数封装后，直接提交给Shiro处理
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);
            //Shiro认证通过后会将user信息放到subject内，生成token并返回
            GPUser GPUser = (GPUser) subject.getPrincipal();
            if(GPUser.getActivecode().equals("Actived")){//验证用户是否被激活了
                String newToken = daUserService.generateJwtToken(GPUser);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=UTF8");
//                response.setHeader("status","SUCCESS");//返回登录结果
                PrintWriter writer = response.getWriter();

                HashMap<String, Object> map1 =new HashMap<String, Object>();
                map1.put("status","SUCCESS");

                HashMap<String, Object> map2 =new HashMap<String, Object>();
                map2.put("userId", String.valueOf(GPUser.getId()));
                map2.put("userName", GPUser.getName());
                map2.put("userPhone", GPUser.getPhone());
                map2.put("userEmail", GPUser.getEmail());
                map2.put("userToken", GPUser.getRemember_token());
                map2.put("userJurisdiction", String.valueOf(GPUser.getJurisdiction()));
                writer.write(map1.toString());
                writer.close();

//                response.setHeader("status","SUCCESS");//返回登录结果
                //添加用户信息
//                response.setHeader("userId", String.valueOf(GPUser.getId()));
//                response.setHeader("userName", GPUser.getName());
//                response.setHeader("userPhone", GPUser.getPhone());
//                response.setHeader("userEmail", GPUser.getEmail());
//                response.setHeader("userToken", GPUser.getRemember_token());
//                response.setHeader("userJurisdiction", String.valueOf(GPUser.getJurisdiction()));

                //对token进行持久化
                GPUser record=new GPUser();
                record.setEmail(email);
                record.setRemember_token(newToken);
                daUserService.updateToken(record);

                System.out.println("验证成功，返回token");
                return ResponseEntity.ok().build();
                //return response;
            }
            else{
                //邮箱未验证
                System.out.println("用户尚未激活！");
                return ResponseEntity.notFound().build();
            }

        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            System.out.println("User "+ email+" login fail, Reason:" +e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        if(subject.getPrincipals() != null) {
            GPUser GPUser = (GPUser)subject.getPrincipals().getPrimaryPrincipal();
            daUserService.deleteLoginInfo(GPUser.getEmail());//根据email删除token
            System.out.println("用户登出成功！");
        }
        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/resendConfirmEmail")
    public Object resendConfirmEmail(String email) throws MessagingException {//重新发送邮箱
        //用户输入邮箱，以及验证码
        //前端验证通过之后，调用该方法
        GPUser GPUser = daUserService.selectUserByEmail(email);//根据用户所输入的邮箱进行查找
        if (GPUser == null) return "邮箱输入错误或者距离注册已经超过24小时";//如果在数据库中找不到该用户
        if (GPUser.getActivecode().equals("Actived")) {//如果已经注册了就返回
            return "邮箱已注册";
        } else {
            mailService.sendMimeMail(GPUser.getEmail(), "欢迎您使用我们的系统！", "请复制访问以下链接进行确认。\nhttp://localhost:8080/emailconfirm?email=" + GPUser.getEmail() + "&activecode=" + GPUser.getActivecode());
        }
        return "邮件以发送，请查收！";
    }
    //忘记密码功能
    @RequestMapping(value = "/forgetPassword")
    public Object forgetPassword(String email,String phone,HttpServletRequest request, HttpServletResponse response) throws MessagingException {
        GPUser GPUser =daUserService.selectUserByEmail(email);
        if (GPUser == null) return "邮箱输入错误或者距离注册已经超过24小时";//如果在数据库中找不到该用户
        if(GPUser.getPhone().equals(phone)){
            //进入验证阶段
            //设置激活码
            daUserService.resetActiveCode(email);
            GPUser =daUserService.selectUserByEmail(email);//重新赋值
            mailService.sendMimeMail(GPUser.getEmail(),"欢迎您使用我们的系统！","您的账号：。\n邮箱："+ GPUser.getEmail()+" 正在修改登录密码，如有问题请联系管理员\n解锁码："+ GPUser.getActivecode());
            response.setHeader("email",email);
            response.setHeader("phone",phone);
            return ResponseEntity.ok().build();//"重置密码邮件以发送，转跳至填写页面resetpassword（激活码，密码）"
        }else{
            return "邮箱与联系方式不匹配。";
        }
    }
    //忘记密码的重新设置功能
    @RequestMapping(value = "/resetPassword")
    public Object resetPassword(String email,String activecode,String password,HttpServletRequest request, HttpServletResponse response){
//        DAUser daUser=new DAUser();
//        daUser.setEmail(request.getHeader("email"));
//        daUser.setActivecode(activecode);
        GPUser GPUser =daUserService.selectUserByEmail(email);
        if (GPUser == null) return "邮箱输入错误或者距离注册已经超过24小时";//如果在数据库中找不到该用户
        if(GPUser.getActivecode().equals(activecode)){
            //此处密码做加盐加密
            String salt= UUID.randomUUID().toString();
            String message=password;
            String encryption= ShiroUtil.encryptionBySalt(salt,message);
            //存储加密后的密码
            GPUser.setPassword(encryption);
            GPUser.setSalt(salt);//存储盐

            daUserService.updatePassword(GPUser);//对密码和盐进行存储
            daUserService.changeActiveCode(email);//将激活码改回Actived
            System.out.println("密码修改成功");
            return "密码修改成功";
        }else{
            System.out.println("激活码不正确");
            return "激活码不正确";
        }
    }


}
