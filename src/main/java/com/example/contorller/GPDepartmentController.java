package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPStaff;
import com.example.service.GPStaffService;
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

@Controller
public class GPDepartmentController {
    @Autowired
    private GPStaffService gpStaffService;

    @RequestMapping(value = "/addStaff")
    public ResponseEntity<HashMap<String, Object>> addStaff(GPStaff staff, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("id:"+staff.getId());
        System.out.println("dept_no:"+staff.getDept_no());

        int i = gpStaffService.insertStaff(staff);
        if (i > 0) {
            jsonObject.put("status", "Success");
            jsonObject.put("message", "员工添加成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "员工添加失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        System.out.println("员工添加成功");
        return ResponseEntity.ok().build();
    }



    @RequestMapping(value = "/getStaffList")
    public ResponseEntity<HashMap<String, Object>> getStaffList(int curr, int pageSize, String keywords,Integer department,HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println(curr);
        System.out.println(pageSize);
        System.out.println(keywords);

        if (keywords.equals("")) {//如果关键字为空，则进行全局搜索
            List<GPStaff> staffList = gpStaffService.selectAllStaff(curr, pageSize,department);
            System.out.println("listSize:" + staffList.size());
            if (!staffList.isEmpty()) {
                System.out.println("开始写入json");
                jsonObject.put("count", gpStaffService.selectStaffCount(department));
                jsonObject.put("curr", curr);
                jsonObject.put("data", staffList);
//                System.out.println(jsonObject.toJSONString());
                System.out.println("写入json完成");
            } else {
                //userList为空的情况
                System.out.println("carList为空！");
                jsonObject.put("message", "未能查询到相关信息！");
            }
        } else {
            //存在关键字的情况
            List<GPStaff> staffList = gpStaffService.selectStaffByKeyword(keywords, curr, pageSize,department);
            System.out.println("listSize:" + staffList.size());
            System.out.println("开始写入json");
            if (staffList.size() > pageSize) {
                int startIndex = (curr - 1) * pageSize;//开始截取的位置
                int toIndex = 0;
                if (staffList.size() - startIndex >= pageSize) {//这进行位置判断，特别是结尾的判断
                    toIndex = startIndex + pageSize - 1;
                } else {
                    toIndex = staffList.size() - 1;//直接定位到结尾
                }
                List<GPStaff> childStaffList = staffList.subList(startIndex, toIndex);//如果tags集合大于40条数据就就截取前35条
                System.out.println("listSize:" + childStaffList.size());
                jsonObject.put("data", childStaffList);
            } else {
                jsonObject.put("data", staffList);
            }
            jsonObject.put("count", staffList.size());
            jsonObject.put("curr", curr);
            System.out.println("写入json完成");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/deleteStaff")
    public ResponseEntity<HashMap<String, Object>> deleteStaff(Integer id, Integer department,HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备删除："+id);

        try {
            gpStaffService.delStaffById(id,department);//交由service层进行删除工作

            System.out.println(id + "  删除完成");
            jsonObject.put("message", "Success");
            System.out.println("写入json完成");
        } catch (Exception exception) {
            System.out.println(id + "删除失败");
            jsonObject.put("error", exception.toString());
            System.out.println("写入json完成");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/selectStaffById")
    public ResponseEntity<HashMap<String, Object>> selectStaffById(Integer id,Integer department, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("单独查询："+id);
        //进行修改前，对用户的详细信息进行查看
        GPStaff gpStaff = gpStaffService.selectStaffById(id,department);

        jsonObject.put("status", "SUCCESS");
        jsonObject.put("staffInfo", gpStaff);

        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/updateStaff")
    public ResponseEntity<HashMap<String, Object>> updateStaff(Integer id,String name,String email,Integer age, String phone, String address, Integer department, String id_no,  String position,HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备开始修改："+id);
        System.out.println(age);
        int result = gpStaffService.updateStaff(id,name,email,age,phone,address,department,id_no,position);
        System.out.println(result);
        if (result == 1) {
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("message", "车辆信息修改成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "车辆信息修改失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/selectCoach")
    public ResponseEntity<HashMap<String, Object>> selectCoach(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("获取教练列表");
        //进行修改前，对用户的详细信息进行查看
        List<GPStaff> staffList = gpStaffService.selectCoach();

        jsonObject.put("status", "SUCCESS");
        jsonObject.put("staffInfo", staffList);

        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }
}
