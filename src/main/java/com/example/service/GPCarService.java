package com.example.service;

import com.example.entity.GPCar;

import java.util.List;

public interface GPCarService {
    //查询所有车辆信息
    List<GPCar> selectAllCar(int curr, int pageSize);

    //查询车辆信息数量
    Object selectCarCount();

    //根据关键字查询车辆信息
    List<GPCar> selectAllCarByKeyword(String keywords, int curr, int pageSize);
}
