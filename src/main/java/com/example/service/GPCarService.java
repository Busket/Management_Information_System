package com.example.service;

import com.example.entity.GPCar;

import java.sql.Timestamp;
import java.util.List;

public interface GPCarService {

    //查询所有车辆信息
    List<GPCar> selectAllCar(int curr, int pageSize);

    //查询车辆信息数量
    Object selectCarCount();

    //根据关键字查询车辆信息
    List<GPCar> selectAllCarByKeyword(String keywords, int curr, int pageSize);

    //添加车辆信息
    Integer insertCar(GPCar car);

    //删除车辆信息
    Integer delCarById(Integer id);

    //修改前查询车辆的详细信息
    GPCar selectCarById(Integer id);

    //修改车辆信息
    int updateCarById(Integer id, String vin, String carNumber, String brand, Integer status, String chargeMan);

    List<GPCar> selectCarByCoach(String coach);
}
