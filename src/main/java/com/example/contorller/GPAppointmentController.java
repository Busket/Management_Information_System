package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPAppointment;
import com.example.entity.GPCar;
import com.example.entity.GPStudent;
import com.example.service.GPAppointmentService;
import com.example.service.GPCarService;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
public class GPAppointmentController {
    @Autowired
    GPAppointmentService gpAppointmentService;
    @Autowired
    GPCarService gpCarService;

    @RequestMapping(value = "/makeAppointment")
    public ResponseEntity<HashMap<String, Object>> makeAppointment(GPAppointment appointment, String dat, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备添加一个预约！");
        System.out.println(appointment.getStudent_no());

        long d = Long.parseLong(dat);
        Timestamp date = new Timestamp(d);
        System.out.println(date.getTime());
        appointment.setDate(date);

        //在这里进行时间的比对
        List<GPAppointment> appointments = gpAppointmentService.selectAppointmentsBySN(appointment.getStudent_no());
        int r = 0;//判别是否有重复
        if (!appointments.isEmpty()) {
            for (GPAppointment ap : appointments) {
                if (Math.round(ap.getDate().getTime() / 100000000) == Math.round(date.getTime() / 100000000)) {
                    System.out.println("预约重复");
                    jsonObject.put("status", "Fail");
                    jsonObject.put("message", "该日期您已经预约练车，无需重复预约！");
                    r++;//如果存在相同日期，则进行累加
                    break;
                }
            }
        }
        if (r == 0) {
            //如果没有重复则进行插入
            int i = gpAppointmentService.insertAppointment(appointment);
            if (i > 0) {
                jsonObject.put("status", "Success");
                jsonObject.put("message", "预约添加成功！");
            } else {
                jsonObject.put("status", "Fail");
                jsonObject.put("message", "预约添加失败！");
            }
        }

        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/studentGetAp")
    public ResponseEntity<HashMap<String, Object>> studentGetAp(String student_no, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println("开始预约搜索记录：" + student_no);
        Timestamp nowTime = new Timestamp(new Date().getTime());
        System.out.println("现在的时间是:" + nowTime);

        List<GPAppointment> appointments = gpAppointmentService.selectAppointmentsBySN(student_no);

        System.out.println("listSize:" + appointments.size());
        System.out.println("开始写入json");
        List<GPAppointment> childAPL = new ArrayList<>();//如果超出三个预约，则只显示最开头的三个
        List<GPAppointment> showApL = new ArrayList<>();//用于传输的
        try {
            if (appointments.isEmpty()) {//没有数据的情况
                jsonObject.put("status", "Empty");
                jsonObject.put("message", "预约列表为空");
            } else {
                System.out.println(appointments.size());

                for (GPAppointment appointment : appointments) {//先进行筛选，选出大于今天的
                    long apTime=appointment.getDate().getTime() / 100000000;
                    if ( apTime >=(nowTime.getTime() / 100000000))//判断日期是否大于等于今天
                    {
                        childAPL.add(appointment);
                    }
                }
                System.out.println("childAPL："+childAPL.size());
                int j = 0;
                if (appointments.isEmpty()) {//没有数据的情况
                    jsonObject.put("status", "Empty");
                    jsonObject.put("message", "预约列表为空");
                } else j = Math.min(childAPL.size(), 3);

                for (int i = 0; i < j; i++) {
                    showApL.add(childAPL.get(i));
                }
                jsonObject.put("status", "Success");
                jsonObject.put("aps", showApL);
                System.out.println("写入aps完成！");
            }
        } catch (Exception e) {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "预约查询失败");
        }
        System.out.println("写入json完成");
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deleteAppointment")
    public ResponseEntity<HashMap<String, Object>> deleteAppointment(Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备删除："+id);

        try {
            Integer i=gpAppointmentService.delAppointmentById(id);//交由service层进行删除工作

            System.out.println(id + "  删除完成");
            jsonObject.put("message", "Success");
        } catch (Exception exception) {
            System.out.println(id + "删除失败");
            jsonObject.put("error", exception.toString());
        }
        System.out.println("写入json完成");
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/coachFindApList")
    public ResponseEntity<HashMap<String, Object>> coachFindApList(String coach, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println("开始预约搜索记录：" + coach);
        Timestamp nowTime = new Timestamp(new Date().getTime());
        System.out.println("现在的时间是:" + nowTime);

        List<GPAppointment> appointments = gpAppointmentService.selectAppointmentsByCoach(coach);

        System.out.println("listSize:" + appointments.size());
        System.out.println("开始写入json");
        try {
            if (appointments.isEmpty()) {//没有数据的情况
                jsonObject.put("status", "Empty");
                jsonObject.put("message", "预约列表为空");
            } else {
                System.out.println(appointments.size());
                List<GPAppointment> showApL = new ArrayList<>();//用于传输的
                for (GPAppointment appointment : appointments) {//先进行筛选，选出大于今天的
                    long apTime=appointment.getDate().getTime() / 100000000;
                    if ( apTime >=(nowTime.getTime() / 100000000))//判断日期是否大于等于今天
                    {
                        showApL.add(appointment);
                    }
                }
                jsonObject.put("status", "Success");
                jsonObject.put("aps", showApL);
                System.out.println("写入aps完成！");
            }
        } catch (Exception e) {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "预约查询失败");
        }
        System.out.println("写入json完成");
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/studentGetCars")
    public ResponseEntity<HashMap<String, Object>> studentGetCars(String coach,String subject, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        System.out.println("开始搜索教练"+coach+"所负责的：" + subject+"的车辆");
        Timestamp nowTime = new Timestamp(new Date().getTime());
        System.out.println("现在的时间是:" + nowTime);


        List<GPCar> cars = gpCarService.selectCarByCoach(coach);
        List<GPCar> childCars = new ArrayList<>();//用于存放筛选结果
        System.out.println("listSize:" + cars.size());
        System.out.println("开始判断车辆状态！");
        for(GPCar car:cars){
            if(subject.equals("科目二")){
                if(car.getStatus()==112){
                    if(!car.getCarNumber().isEmpty()){
                        childCars.add(car);
                    }
                }
            }else if(subject.equals("科目三")){
                if(car.getStatus()==113){
                    if(!car.getCarNumber().isEmpty()){
                        childCars.add(car);
                    }
                }
            }
        }

            if (childCars.isEmpty()) {//没有数据的情况
                jsonObject.put("status", "Empty");
                jsonObject.put("message", "预约列表为空");
            } else {

                jsonObject.put("status", "Success");
                jsonObject.put("cars", childCars);
                System.out.println("写入cars完成！");
            }

        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }

}
