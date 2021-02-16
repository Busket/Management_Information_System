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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GPUserController {
    @Autowired
    private GPUserService GPUserService;
    @RequestMapping(value = "/getUserList")
    public ResponseEntity<HashMap<String, Object>> getUserList(int curr, int pageSize, String keywords, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Subject subject = SecurityUtils.getSubject();
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
//            System.out.println(userList.get(2).getId());
            System.out.println(userList.size());
            if (!userList.isEmpty()) {
                System.out.println("开始写入json");
                jsonObject.put("count", GPUserService.selectUserCount());
                jsonObject.put("curr",curr);
                System.out.println(jsonObject.toJSONString());
                jsonObject.put("data", userList);
                System.out.println("写入json完成");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.ok().build();
            }else{
                //userList为空的情况
                return ResponseEntity.notFound().build();
            }
        } else {
            //存在关键字的情况
            return ResponseEntity.notFound().build();
        }
//        return ResponseEntity.notFound().build();
    }
//        try {
//
//            //Shiro认证通过后会将user信息放到subject内，生成token并返回
//            GPUser GPUser = (GPUser) subject.getPrincipal();
//            if (GPUser.getActivecode().equals("Actived")) {//验证用户是否被激活了
//                String newToken = daUserService.generateJwtToken(GPUser);
//
//
//                map.put("userId", String.valueOf(GPUser.getId()));
//                map.put("userName", GPUser.getName());
//                map.put("userPhone", GPUser.getPhone());
//                map.put("userEmail", GPUser.getEmail());
//                map.put("userToken", GPUser.getRemember_token());
//                map.put("userJurisdiction", String.valueOf(GPUser.getJurisdiction()));
//                jsonObject.put("status", "SUCCESS");
//                jsonObject.put("userInfo", map);
//                writer.write(jsonObject.toJSONString());
//                writer.close();
//
//                //对token进行持久化
//                GPUser record = new GPUser();
//                record.setEmail(email);
//                record.setRemember_token(newToken);
//                daUserService.updateToken(record);
//
//                System.out.println("验证成功，返回token");
//                return ResponseEntity.ok().build();
//            }
//        }catch (AuthenticationException e) {
//            // 如果校验失败，shiro会抛出异常，返回客户端失败
//            jsonObject.put("message", "账户或者密码不正确！请查证后再次登录！");
//            writer.write(jsonObject.toJSONString());
//            writer.close();
//            System.out.println("User " + email + " login fail, Reason:" + e.getMessage());
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            return ResponseEntity.ok().build();
//        }
//        return null;
//    }
}
