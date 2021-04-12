package com.example.service.impl;

import com.example.entity.GPAppointment;
import com.example.entity.GPCar;
import com.example.repository.GPAppointmentMapper;
import com.example.repository.GPCarMapper;
import com.example.service.GPAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GPAppointmentimpl implements GPAppointmentService {


    @Autowired
    private GPAppointmentMapper gpAppointmentMapper;
    @Override
    public int insertAppointment(GPAppointment appointment) {
        //获取timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        //此处设置creat_at
        appointment.setCreate_at(t);
        appointment.setUpdate_at(t);
        return gpAppointmentMapper.insertAppointment(appointment);
    }

    @Override
    public List<GPAppointment> selectAppointmentsBySN(String student_no) {
        return gpAppointmentMapper.selectAppointmentsBySN(student_no);
    }

    @Override
    public Integer delAppointmentById(Integer id) {
        return gpAppointmentMapper.delAppointmentById(id);
    }

    @Override
    public List<GPAppointment> selectAppointmentsByCoach(String coach) {
        return gpAppointmentMapper.selectAppointmentsByCoach(coach);
    }



}
