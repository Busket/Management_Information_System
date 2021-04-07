package com.example.service.impl;

import com.example.entity.GPStaff;
import com.example.repository.GPCarMapper;
import com.example.repository.GPStaffMapper;
import com.example.service.GPStaffService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GPStaffimpl implements GPStaffService {
    @Autowired
    private GPStaffMapper gpStaffMapper;

    @Override
    public Integer insertStaff(GPStaff staff) {
        //获取timestamp
        Timestamp t = new Timestamp(System.currentTimeMillis());
        //此处设置creat_at
        staff.setCreate_at(t);
        staff.setUpdate_at(t);
        String table = "";
        //根据部门给工号前加上部门标识
        if (staff.getDepartment() == 211) {
            if (!staff.getDept_no().startsWith("BGS")) {
                staff.setDept_no("BGS" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 212) {
            if (!staff.getDept_no().startsWith("XXZX")) {
                staff.setDept_no("XXZX" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 213) {
            if (!staff.getDept_no().startsWith("JLK")) {
                staff.setDept_no("JLK" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 214) {
            if (!staff.getDept_no().startsWith("JWK")) {
                staff.setDept_no("JWK" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 215) {
            if (!staff.getDept_no().startsWith("YWK")) {
                staff.setDept_no("YWK" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 216) {
            if (!staff.getDept_no().startsWith("CWK")) {
                staff.setDept_no("CWK" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 217) {
            if (!staff.getDept_no().startsWith("ZWK")) {
                staff.setDept_no("ZWK" + staff.getDept_no());
            }
        } else if (staff.getDepartment() == 218) {
            if (!staff.getDept_no().startsWith("LBC")) {
                staff.setDept_no("LBC" + staff.getDept_no());
            }
        } else {
            return -55;//返回出错
        }
        return gpStaffMapper.insertStaff(staff);
    }

    @Override
    public List<GPStaff> selectAllStaff(int curr, int pageSize, Integer department) {
        int row=(curr-1)*pageSize;
        return gpStaffMapper.selectAllStaff(row,pageSize,department);
    }

    @Override
    public Integer selectStaffCount(Integer department) {
        return gpStaffMapper.selectStaffCount(department);
    }

    @Override
    public List<GPStaff> selectStaffByKeyword(String keywords, int curr, int pageSize, Integer department) {
        return gpStaffMapper.selectStaffByKeyword(keywords+"%","%"+keywords+"%","%"+keywords,department);
    }

    @Override
    public Integer delStaffById(Integer id, Integer department) {
        return gpStaffMapper.delStaffById(id,department);
    }

    @Override
    public GPStaff selectStaffById(Integer id, Integer department) {
        return gpStaffMapper.selectStaffById(id,department);
    }

    @Override
    public Integer updateStaff(Integer id, String name, String email,Integer age, String phone, String address, Integer department, String id_no, String position) {
        //获取timestamp
        Timestamp update_at = new Timestamp(System.currentTimeMillis());
        return gpStaffMapper.updateStaff(id,name, email,age, phone, address, department, id_no, position,update_at);
    }

    @Override
    public List<GPStaff> selectCoach() {
        return gpStaffMapper.selectCoach();
    }

    @Override
    public GPStaff selectCoachByNumber(String Number) {
        return gpStaffMapper.selectCoachByNumber(Number);
    }

    @Override
    public GPStaff selectStaffByEmail(String email) {
            return gpStaffMapper.selectStaffByEmail(email);
    }
}


