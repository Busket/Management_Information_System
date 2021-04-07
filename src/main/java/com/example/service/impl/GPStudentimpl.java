package com.example.service.impl;

import com.example.entity.GPStudent;
import com.example.entity.GPUser;
import com.example.repository.GPStudentMapper;
import com.example.service.GPStudnetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GPStudentimpl implements GPStudnetService {
    @Autowired
    private GPStudentMapper gpStudentMapper;
    //添加学生
    @Override
    public Integer insertStudent(GPStudent student) {
        //获取timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        //此处设置creat_at
        student.setCreate_at(t);
        student.setUpdate_at(t);
        //设置学生学号
        student.setNumber("STU"+(gpStudentMapper.selectStudentCount()+1));
        return gpStudentMapper.insertStudent(student);
    }

    @Override
    public Integer selectStudentCount() {
        return gpStudentMapper.selectStudentCount();
    }

    @Override
    public List<GPStudent> selectAllStudent(int curr, int pageSize) {
        int row=(curr-1)*pageSize;
        return gpStudentMapper.selectAllStudent(row,pageSize);//读图个参数用于确定行数，从哪开始拿，第二个确定数量
    }

    //关键次查询学生
    @Override
    public List<GPStudent> selectAllStudentByKeyword(String keywords, int curr, int pageSize) {
        return gpStudentMapper.selectAllStudentByKeyword(keywords+"%","%"+keywords+"%","%"+keywords);
    }

    @Override
    public GPStudent selectStudentById(Integer id) {
        return gpStudentMapper.selectStudentById(id);
    }

    @Override
    public int updateStudentById(GPStudent gpStudent) {
        gpStudent.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        return gpStudentMapper.updateStudentById(gpStudent);
    }

    @Override
    public Integer delStudentById(Integer id) {
        return gpStudentMapper.delStudentById(id);
    }

    @Override
    public GPStudent selectStudentByEmail(String email) {
        return gpStudentMapper.selectStudentByEmail(email);
    }

    @Override
    public List<GPStudent> selectCoachStudent(int curr, int pageSize, String coach) {
        int row=(curr-1)*pageSize;
        return gpStudentMapper.selectCoachStudent(row,pageSize,coach);//读图个参数用于确定行数，从哪开始拿，第二个确定数量
    }

    @Override
    public List<GPStudent> selectCoachStudentByKeyword(String keywords, int curr, int pageSize, String coach) {
        return gpStudentMapper.selectCoachStudentByKeyword(keywords+"%","%"+keywords+"%","%"+keywords,coach);
    }
}
