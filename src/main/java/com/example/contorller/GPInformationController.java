package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPAppointment;
import com.example.entity.GPStudent;
import com.example.entity.GPUser;
import com.example.service.GPAppointmentService;
import com.example.service.GPStudnetService;
import com.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class GPInformationController {
    @Autowired
    MailService mailService;//用于发送确认邮件
    @Autowired
    GPAppointmentService gpAppointmentService;
    @Autowired
    GPStudnetService gpStudnetService;

    //这里是行政 功能当中的信息平台，发送通知给学生的（懒得创建新的controller
    @RequestMapping(value = "/sendInformation")
    public ResponseEntity<HashMap<String, Object>> sendInformation(String name, String coach_no, String car_no, String date, String reason, HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        List<String> sendFailStu = new ArrayList<String>();
        Timestamp nowTime = new Timestamp(new Date().getTime());
        System.out.println("准备发送信息！");
        if (car_no.isEmpty()) {
            System.out.println("name:" + name);
            System.out.println("coach_no:" + coach_no);
            List<GPAppointment> gpAppointments = gpAppointmentService.selectAppointmentsByCoach(coach_no);//查找学生信息

            Timestamp d = new Timestamp(Long.parseLong(date));
            int i = 0;//计数
            for (GPAppointment appointment : gpAppointments) {
                System.out.println("apDate:" + appointment.getDate());
                System.out.println("date:" + date);
                long apTime = appointment.getDate().getTime() / 100000000;
                String[] da = d.toString().split(" ");
                System.out.println(da[0]);

                if (apTime >= (nowTime.getTime() / 100000000) && Math.round(apTime) == Math.round(d.getTime() / 100000000))//判断日期是否大于等于今天,且预约日期于教练无法教学日期相等
                {
                    GPStudent gpStudent = gpStudnetService.selectStudentByNumber(appointment.getStudent_no());//查找学生的邮箱
                    System.out.println("开始向" + gpStudent.getEmail() + "发送邮件！");
                    i++;
                    try {
                        mailService.sendMimeMail(gpStudent.getEmail(), "畅途驾校管理系统：\n", "您的教练: " + name + " 于 " + da[0] + " 由于 " + reason + " 的原因，无法给您上课，我们将给您临时更换教练，完成教学任务，尽量避免耽误您的学习进程!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("邮件未能成功发送的学生联系方式：" + gpStudent.getPhone());
                        sendFailStu.add(gpStudent.getPhone());
                    }
                }
            }
            if (i == 0) {
                jsonObject.put("status", "Success");
                jsonObject.put("message", "无学生需要通知！");
            } else if (sendFailStu.size() == 0) {
                jsonObject.put("status", "Success");
                jsonObject.put("message", "全部学员信息发送成功！");
            } else {
                jsonObject.put("status", "Warm");
                jsonObject.put("message", "部分学员信息发送不成功！请电话通知！");
                jsonObject.put("sendFailStu", sendFailStu);
            }
        } else {
            System.out.println("car_no:" + car_no);
            List<GPAppointment> gpAppointments = gpAppointmentService.selectAppointmentsByCarNumber(car_no);//查找学生信息

            Timestamp d = new Timestamp(Long.parseLong(date));
            int i = 0;//计数
            for (GPAppointment appointment : gpAppointments) {
                System.out.println("apDate:" + appointment.getDate());
                System.out.println("date:" + date);
                long apTime = appointment.getDate().getTime() / 100000000;
                String[] da = d.toString().split(" ");
                System.out.println(da[0]);

                if (apTime >= (nowTime.getTime() / 100000000) && Math.round(apTime) == Math.round(d.getTime() / 100000000))//判断日期是否大于等于今天,且预约日期于教练无法教学日期相等
                {
                    GPStudent gpStudent = gpStudnetService.selectStudentByNumber(appointment.getStudent_no());
                    i++;
                    System.out.println("开始向" + gpStudent.getEmail() + "发送邮件！");
                    try {
                        mailService.sendMimeMail(gpStudent.getEmail(), "畅途驾校管理系统：\n", "您的教练: " + name + " 于 " + da[0] + " 由于 " + reason + " 的原因，无法给您上课，我们将给您临时更换教练，完成教学任务，尽量避免耽误您的学习进程!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("邮件未能成功发送的学生联系方式：" + gpStudent.getPhone());
                        sendFailStu.add(gpStudent.getPhone());
                    }
                }
            }
            if (i == 0) {
                jsonObject.put("status", "Success");
                jsonObject.put("message", "无学生需要通知！");
            } else if (sendFailStu.size() == 0) {
                jsonObject.put("status", "Success");
                jsonObject.put("message", "全部学员信息发送成功！");
            } else {
                jsonObject.put("status", "Warm");
                jsonObject.put("message", "部分学员信息发送不成功！请电话通知！");
                jsonObject.put("sendFailStu", sendFailStu);
            }
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }
}
