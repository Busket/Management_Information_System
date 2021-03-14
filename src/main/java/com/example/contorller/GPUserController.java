package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPUser;
import com.example.service.GPUserService;
import com.github.pagehelper.PageInfo;
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
        Map<String, Object> map = new HashMap<String, Object>();
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
            System.out.println(email + "删除完成");
            jsonObject.put("error", exception.toString());
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/addUser")
    public ResponseEntity<HashMap<String, Object>> addUser(GPUser user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(user.getEmail()+"注册，权限为："+user.getJurisdiction());
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
}
