package com.example.repository;

import com.example.entity.GPCar;
import com.example.entity.GPUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GPCarMapper {

    //查询所有车辆信息
    List<GPCar> selectAllCar(int row, int pageSize);
    //查询车辆信息数量
    Object selectCarCount();
    //根据关键字查询车辆信息
    List<GPCar> selectAllCarByKeyword(String keywords1,String keywords2,String keywords3);
}
