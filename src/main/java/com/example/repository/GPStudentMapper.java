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

    List<GPStudent> selectAllStudentByKeyword(String keywords1, String keywords2, String keywords3);

    GPStudent selectStudentById(Integer id);

    int updateStudentById(GPStudent gpStudent);

    Integer delStudentById(Integer id);

    GPStudent selectStudentByEmail(String email);

    List<GPStudent> selectCoachStudent(int row, int pageSize, String coach);

    List<GPStudent> selectCoachStudentByKeyword(String keywords1, String keywords2, String keywords3, String coach);
}
