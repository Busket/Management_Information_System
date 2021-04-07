package com.example.service;

import com.example.entity.GPAppointment;

import java.util.List;

public interface GPAppointmentService {
    int insertAppointment(GPAppointment appointment);

    List<GPAppointment> selectAppointmentsBySN(String student_no);

    Integer delAppointmentById(Integer id);

    List<GPAppointment> selectAppointmentsByCoach(String coach);
}
