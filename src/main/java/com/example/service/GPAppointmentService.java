package com.example.service;

import com.example.entity.GPAppointment;
import com.example.entity.GPCar;

import java.util.List;

public interface GPAppointmentService {
    int insertAppointment(GPAppointment appointment);

    List<GPAppointment> selectAppointmentsBySN(String student_no);

    Integer delAppointmentById(Integer id);

    List<GPAppointment> selectAppointmentsByCoach(String coach);

    List<GPAppointment> selectAppointmentsByCarNumber(String car_no);
}
