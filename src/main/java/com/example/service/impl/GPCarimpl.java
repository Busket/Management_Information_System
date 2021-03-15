package com.example.service.impl;

import com.example.entity.GPCar;
import com.example.repository.GPCarMapper;
import com.example.service.GPCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service

public class GPCarimpl implements GPCarService {

    @Autowired
    private GPCarMapper gpCarMapper;
    //查询所有车辆信息
    @Override
    public List<GPCar> selectAllCar(int curr, int pageSize){
        int row=(curr-1)*pageSize;
        return gpCarMapper.selectAllCar(row,pageSize);
    }
    //查询车辆信息数量
    @Override
    public Object selectCarCount() {
        return gpCarMapper.selectCarCount();
    }
    //根据关键字查询车辆信息
    @Override
    public List<GPCar> selectAllCarByKeyword(String keywords, int curr, int pageSize) {
        return gpCarMapper.selectAllCarByKeyword(keywords+"%","%"+keywords+"%","%"+keywords);
    }
    //添加车辆信息
    @Override
    public Integer insertCar(GPCar car){
        //获取timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        //此处设置creat_at
        car.setCreate_at(t);
        car.setUpdate_at(t);
        return gpCarMapper.insertCar(car);
    }

    @Override
    public Integer delCarById(Integer id) {
        return gpCarMapper.delCarById(id);
    }

    @Override
    public GPCar selectCarById(Integer id) {
        return gpCarMapper.selectCarById(id);
    }

    @Override
    public int updateCarById(Integer id, String vin, String carNumber, String brand, Integer status, String chargeMan) {
        Timestamp update_at=new Timestamp(System.currentTimeMillis());
        return gpCarMapper.updateCarById(id, vin, carNumber, brand, status,chargeMan,update_at);
    }
}
