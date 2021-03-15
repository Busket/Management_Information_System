package com.example.repository;

import com.example.entity.GPCar;
import com.example.entity.GPUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface GPCarMapper {

    //查询所有车辆信息
    List<GPCar> selectAllCar(int row, int pageSize);

    //查询车辆信息数量
    Object selectCarCount();

    //根据关键字查询车辆信息
    List<GPCar> selectAllCarByKeyword(String keywords1, String keywords2, String keywords3);

    //添加车辆信息
    Integer insertCar(GPCar car);

    //删除车辆信息
    Integer delCarById(Integer id);

    //修改前查询车辆的详细信息
    GPCar selectCarById(Integer id);

    //修改指定id的车辆信息
    Integer updateCarById(Integer id, String vin, String carNumber, String brand, Integer status, String chargeMan, Timestamp update_at);
}
