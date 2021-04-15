package com.example.repository;

import com.example.entity.GPAppointment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GPAppointmentMapper {

    int insertAppointment(GPAppointment appointment);

    List<GPAppointment> selectAppointmentsBySN(String student_no);

    Integer delAppointmentById(Integer id);

    List<GPAppointment> selectAppointmentsByCoach(String coach);

    List<GPAppointment> selectAppointmentsByCarNumber(String car_no);
}
