package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPStaff;
import com.example.entity.GPStudent;
import com.example.entity.GPUser;
import com.example.service.GPStaffService;
import com.example.service.GPStudnetService;
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
public class GPStudentController {
    @Autowired
    private GPStudnetService gpStudnetService;

    @RequestMapping(value = "/addStudent")
    public ResponseEntity<HashMap<String, Object>> addStudent(GPStudent student, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备添加一位学生！");
        int i = gpStudnetService.insertStudent(student);
        if (i > 0) {
            jsonObject.put("status", "Success");
            jsonObject.put("message", "学生添加成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "学生添加失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/getStudentList")
    public ResponseEntity<HashMap<String, Object>> getStudentList(int curr, int pageSize, String keywords, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println(curr);
        System.out.println(pageSize);
        System.out.println(keywords);

        if (keywords.equals("")) {//如果关键字为空，则进行全局搜索
            List<GPStudent> studentList = gpStudnetService.selectAllStudent(curr, pageSize);
            System.out.println("listSize:" + studentList.size());
            if (!studentList.isEmpty()) {
                System.out.println("开始写入json");
                jsonObject.put("count", gpStudnetService.selectStudentCount());
                jsonObject.put("curr", curr);
                System.out.println(jsonObject.toJSONString());
                jsonObject.put("data", studentList);
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
            List<GPStudent> studentList = gpStudnetService.selectAllStudentByKeyword(keywords, curr, pageSize);
            System.out.println("listSize:" + studentList.size());
            System.out.println("开始写入json");
//                System.out.println(userList.get(1).getCreat_at().toString());
            if (studentList.size() > pageSize) {
                int startIndex = (curr - 1) * pageSize;//开始截取的位置
                int toIndex = 0;
                if (studentList.size() - startIndex >= pageSize) {//这进行位置判断，特别是结尾的判断
                    toIndex = startIndex + pageSize - 1;
                } else {
                    toIndex = studentList.size() - 1;//直接定位到结尾
                }
                List<GPStudent> childStudentList = studentList.subList(startIndex, toIndex);//如果tags集合大于40条数据就就截取前35条
                System.out.println("listSize:" + childStudentList.size());
                jsonObject.put("data", childStudentList);
            } else {
                jsonObject.put("data", studentList);
            }
            jsonObject.put("count", studentList.size());
            jsonObject.put("curr", curr);
//                System.out.println(jsonObject.toJSONString());
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }
}
