package com.example.service;

import com.example.entity.GPStaff;

import java.util.List;

public interface GPStaffService {
    //添加员工
    Integer insertStaff(GPStaff staff);
//查找所有员工
    List<GPStaff> selectAllStaff(int curr, int pageSize, Integer department);
//查找员工数量
    Integer selectStaffCount(Integer department);
//关键字搜索（名字）
    List<GPStaff> selectStaffByKeyword(String keywords, int curr, int pageSize, Integer department);
//删除员工信息
    Integer delStaffById(Integer id, Integer department);
//通过id和部门查找员工
    GPStaff selectStaffById(Integer id, Integer department);
//修改员工信息
    Integer updateStaff(Integer id, String name, String email,Integer age, String phone, String address, Integer department, String id_no, String position);
}
