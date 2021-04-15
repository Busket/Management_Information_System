package com.example.service;

import com.example.entity.GPStudent;

import java.util.List;

public interface GPStudnetService {
    Integer insertStudent(GPStudent student);

    Integer selectStudentCount();

    List<GPStudent> selectAllStudent(int curr, int pageSize);

    List<GPStudent> selectAllStudentByKeyword(String keywords, int curr, int pageSize);

    GPStudent selectStudentById(Integer id);

    int updateStudentById(GPStudent gpStudent);

    Integer delStudentById(Integer id);

    GPStudent selectStudentByEmail(String email);

    List<GPStudent> selectCoachStudent(int curr, int pageSize, String coach);

    List<GPStudent> selectCoachStudentByKeyword(String keywords, int curr, int pageSize, String coach);

    GPStudent selectStudentByNumber(String student_no);
}
