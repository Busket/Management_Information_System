package com.example.repository;

import com.example.entity.GPStaff;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface GPStaffMapper {
    Integer insertStaff(GPStaff staff);//前面是数据，后面是根据部门不同，插入不同的表中去

    List<GPStaff> selectAllStaff(int row, int pageSize,Integer department);

    Integer selectStaffCount(Integer department);

    List<GPStaff> selectStaffByKeyword(String keywords1, String keywords2, String keywords3,Integer department);

    Integer delStaffById(Integer id, Integer department);

    GPStaff selectStaffById(Integer id, Integer department);

    Integer updateStaff(Integer id, String name, String email, Integer age, String phone, String address, Integer department, String id_no, String position, Timestamp update_at);

    List<GPStaff> selectCoach();
}
