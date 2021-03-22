package com.example.service;

import com.example.entity.GPStudent;
import com.example.entity.GPUser;

import java.util.List;

public interface GPStudnetService {
    Integer insertStudent(GPStudent student);

    Integer selectStudentCount();

    List<GPStudent> selectAllStudent(int curr, int pageSize);

    List<GPStudent> selectAllStudentByKeyword(String keywords, int curr, int pageSize);

    GPStudent selectStudentById(Integer id);

    int updateStudentById(GPStudent gpStudent);

    Integer delStudentById(Integer id);
}
