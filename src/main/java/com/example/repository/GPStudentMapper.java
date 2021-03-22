package com.example.repository;

import com.example.entity.GPStudent;
import com.example.entity.GPUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GPStudentMapper {
    Integer insertStudent(GPStudent student);

    Integer selectStudentCount();

    List<GPStudent> selectAllStudent(int row, int pageSize);

    List<GPStudent> selectAllStudentByKeyword(String s, String s1, String s2);

    GPStudent selectStudentById(Integer id);

    int updateStudentById(GPStudent gpStudent);

    Integer delStudentById(Integer id);
}
