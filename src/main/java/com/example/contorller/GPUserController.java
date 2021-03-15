package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPUser;
import com.example.service.GPUserService;
import com.example.shiro.ShiroUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class GPUserController {
    @Autowired
    private GPUserService GPUserService;

    @RequestMapping(value = "/getUserList")
    public ResponseEntity<HashMap<String, Object>> getUserList(int curr, int pageSize, String keywords, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(curr);
        System.out.println(pageSize);
        System.out.println(keywords);

        if (keywords.equals("")) {//如果关键字为空，则进行全局搜索
            List<GPUser> userList = GPUserService.selectAllUser(curr, pageSize);
            System.out.println("listSize:" + userList.size());
            if (!userList.isEmpty()) {
                System.out.println("开始写入json");
                jsonObject.put("count", GPUserService.selectUserCount());
                jsonObject.put("curr", curr);
                System.out.println(jsonObject.toJSONString());
                jsonObject.put("data", userList);
                System.out.println("写入json完成");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.ok().build();
            } else {
                //userList为空的情况
                System.out.println("userList为空！");
                jsonObject.put("message", "未能查询到相关信息！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.notFound().build();
            }
        } else {
            //存在关键字的情况
            List<GPUser> userList = GPUserService.selectAllUserByKeyword(keywords, curr, pageSize);
            System.out.println("listSize:" + userList.size());
            System.out.println("开始写入json");
//                System.out.println(userList.get(1).getCreat_at().toString());
            if (userList.size() > pageSize) {
                int startIndex = (curr - 1) * pageSize;//开始截取的位置
                int toIndex = 0;
                if (userList.size() - startIndex >= pageSize) {//这进行位置判断，特别是结尾的判断
                    toIndex = startIndex + pageSize - 1;
                } else {
                    toIndex = userList.size() - 1;//直接定位到结尾
                }
                List<GPUser> childUserList = userList.subList(startIndex, toIndex);//如果tags集合大于40条数据就就截取前35条
                System.out.println("listSize:" + childUserList.size());
                jsonObject.put("data", childUserList);
            } else {
                jsonObject.put("data", userList);
            }
            jsonObject.put("count", userList.size());
            jsonObject.put("curr", curr);
//                System.out.println(jsonObject.toJSONString());
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/deleteUser")
    public ResponseEntity<HashMap<String, Object>> deleteUser(String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(email);

        try {
            GPUserService.delUserByEmail(email);
            System.out.println(email + "删除完成");
            jsonObject.put("message", "Success");
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            System.out.println(email + "删除失败");
            jsonObject.put("error", exception.toString());
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/addUser")
    public ResponseEntity<HashMap<String, Object>> addUser(GPUser user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(user.getEmail() + "注册，权限为：" + user.getJurisdiction());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        int i = GPUserService.insertSelective(user);
        if (i > 0) {
            jsonObject.put("status", "Success");
            jsonObject.put("message", "用户添加成功！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            System.out.println("注册成功");
            return ResponseEntity.ok().build();
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "用户添加失败！");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/selectUserByEmail")
    public ResponseEntity<HashMap<String, Object>> selectUserByEmail(String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(email);
        //进行修改前，对用户的详细信息进行查看
        GPUser gpUser = GPUserService.selectUserByEmail(email);

        jsonObject.put("status", "SUCCESS");
        jsonObject.put("userInfo", gpUser);
        writer.write(jsonObject.toJSONString());
        writer.close();

        return ResponseEntity.ok().build();
    }

    //忘记密码的重新设置功能
    @RequestMapping(value = "/resetUserPassword")
    public ResponseEntity<HashMap<String, Object>> resetUserPassword(String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(email);
        try {
            GPUser GPUser = GPUserService.selectUserByEmail(email);
            String newPassword = "123456";
            //此处密码做加盐加密
            String salt = UUID.randomUUID().toString();
            String message = newPassword;
            String encryption = ShiroUtil.encryptionBySalt(salt, message);
            //存储加密后的密码
            GPUser.setPassword(encryption);
            GPUser.setSalt(salt);//存储盐
            GPUserService.updatePassword(GPUser);//对密码和盐进行存储
            System.out.println("密码重置成功");

            jsonObject.put("status", "SUCCESS");
            jsonObject.put("message", "密码重置成功！");
            writer.write(jsonObject.toJSONString());
            writer.close();

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "密码重置失败！");
            writer.write(jsonObject.toJSONString());
            writer.close();

            return ResponseEntity.ok().build();
        }
    }

    //修改用户信息
    @RequestMapping(value = "/updateUser")
    public ResponseEntity<HashMap<String, Object>> updateUser(Integer id, String name, String email, String phone, Integer jurisdiction, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(id);

        int result = GPUserService.updateUserById(id, email, name, phone, jurisdiction);
        System.out.println(result);
        if (result == 1) {
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("message", "用户信息修改成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "用户信息修改失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

}
