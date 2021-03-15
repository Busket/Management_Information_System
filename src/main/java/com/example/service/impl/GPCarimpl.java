package com.example.service.impl;

import com.example.entity.GPCar;
import com.example.repository.GPCarMapper;
import com.example.service.GPCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
