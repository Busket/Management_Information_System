package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPUser;
import com.example.service.DAUserService;
import com.example.service.MailService;
import com.example.util.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GPRegisterController {
    @Autowired
    private DAUserService daUserService;//用户的service层
    @Autowired
    MailService mailService;//用于发送确认邮件

    //用户注册
    @RequestMapping("/register")
    public ResponseEntity<HashMap<String, Object>> register(GPUser GPUser, HttpServletRequest request, HttpServletResponse response) throws MessagingException, IOException {
        System.out.println("注册");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        int i = daUserService.insertSelective(GPUser);
        //这个应该是用来检测插入是否成功的
//        Map map = new HashMap<>();
        if (i > 0) {
//            map.put("code", ConstantUtils.successCode);
//            map.put("message", ConstantUtils.insertSuccessMsg);
            jsonObject.put("status", "Success");
            jsonObject.put("message", "注册成功！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("注册成功");

            //注册成功后，发送确认邮件
            //mailService.sendSimpleMailMessage(daUser.getEmail(),"欢迎您使用我们的系统！","请单击以下链接进行确认。");
            //发送带有邮箱以及激活码的链接，点击后进行确认操作
//            mailService.sendMimeMail(daUser.getEmail(),"欢迎您使用我们的系统！","请单击以下链接进行确认。\n" +
//                   "<a href=\"http://localhost:8080/emailconfirm?email="+daUser.getEmail()+"&activecode="+daUser.getActivecode()+"\">激活请点击:这里</a>");
            mailService.sendMimeMail(GPUser.getEmail(), "畅途驾校管理系统：\n欢迎您使用我们的系统！", "请复制以下激活码进行确认。\n邮箱：" + GPUser.getEmail() + "\n激活码：" + GPUser.getActivecode());
            return ResponseEntity.ok().build();
        } else {
//            map.put("code", ConstantUtils.failCode);
//            map.put("message", ConstantUtils.insertFailMsg);
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "注册失败！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping("/emailConfirm")
    public ResponseEntity<HashMap<String, Object>> emailConfirm(String email, String activeCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println(email + "邮箱确认");
        int i = daUserService.checkActiveCodebyEmail(email, activeCode);//验证激活码是否正确
        System.out.println(i);
        //可能需要考虑到重复验证的问题，即已经验证了的，依旧去单击连接
        switch (i) {
            case 1: {
                System.out.println("激活成功！");
                if (daUserService.changeActiveCode(email) == 1) {//确认邮箱成功后，将激活码改为Active
                    daUserService.changeActiveTime(email);//只有在激活码修改成功后才能对其注册时间进行修改
                    jsonObject.put("status", "Success");
                    jsonObject.put("message", "激活成功！");
                    writer.write(jsonObject.toJSONString());
                    writer.close();
                    System.out.println(email+"激活成功！");
                    return ResponseEntity.ok().build();
                }
            }
            case 0: {
                jsonObject.put("status", "Fail");
                jsonObject.put("message", "验证失败！找不到该用户");
                writer.write(jsonObject.toJSONString());
                writer.close();
                System.out.println("验证失败！找不到该用户");
                return ResponseEntity.ok().build();
            }
            case -1: {
                jsonObject.put("status", "Authenticated");
                jsonObject.put("message", "已验证，请勿重复验证！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                System.out.println("重复验证");
                return ResponseEntity.ok().build();
            }
            default: {
                jsonObject.put("status", "Error");
                jsonObject.put("message","发生错误！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                System.out.println(email+"发生错误！");
                return ResponseEntity.ok().build();
            }
        }
    }

    @RequestMapping(value = "/resendConfirmEmail")
    public ResponseEntity<HashMap<String, Object>> resendConfirmEmail(String email, HttpServletRequest request, HttpServletResponse response) throws MessagingException, IOException {//重新发送邮箱
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println(email);
        //前端验证通过之后，调用该方法
        GPUser GPUser = daUserService.selectUserByEmail(email);//根据用户所输入的邮箱进行查找
        if (GPUser == null) {//如果在数据库中找不到该用户
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "验证失败！找不到该用户");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("验证失败！找不到该用户");
        }
        if (GPUser.getActivecode().equals("Actived")) {//如果已经激活了就返回
            jsonObject.put("status", "Authenticated");
            jsonObject.put("message", "已验证，请勿重复验证！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("重复验证");
        } else {
            mailService.sendMimeMail(GPUser.getEmail(), "畅途驾校管理系统：\n欢迎您使用我们的系统！", "请复制以下激活码进行确认。\n邮箱：" + GPUser.getEmail() + "\n激活码：" + GPUser.getActivecode());

            jsonObject.put("status", "Success");
            jsonObject.put("message", "邮件已发送！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("已重新发送邮件！");
        }
        return ResponseEntity.ok().build();
    }

}
