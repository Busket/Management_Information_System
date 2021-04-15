package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPCar;
import com.example.service.GPCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GPCarController {
    @Autowired
    private GPCarService gpCarService;


    @RequestMapping(value = "/getCarList")
    public ResponseEntity<HashMap<String, Object>> getCarList(int curr, int pageSize, String keywords, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(curr);
        System.out.println(pageSize);
        System.out.println(keywords);

        if (keywords.equals("")) {//如果关键字为空，则进行全局搜索
            List<GPCar> carList = gpCarService.selectAllCar(curr, pageSize);
            System.out.println("listSize:" + carList.size());
            if (!carList.isEmpty()) {
                System.out.println("开始写入json");
                jsonObject.put("count", gpCarService.selectCarCount());
                jsonObject.put("curr", curr);
                System.out.println(jsonObject.toJSONString());
                jsonObject.put("data", carList);
                System.out.println("写入json完成");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.ok().build();
            } else {
                //userList为空的情况
                System.out.println("carList为空！");
                jsonObject.put("message", "未能查询到相关信息！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.notFound().build();
            }
        } else {
            //存在关键字的情况
            List<GPCar> carList = gpCarService.selectAllCarByKeyword(keywords, curr, pageSize);
            System.out.println("listSize:" + carList.size());
            System.out.println("开始写入json");
            if (carList.size() > pageSize) {
                int startIndex = (curr - 1) * pageSize;//开始截取的位置
                int toIndex = 0;
                if (carList.size() - startIndex >= pageSize) {//这进行位置判断，特别是结尾的判断
                    toIndex = startIndex + pageSize - 1;
                } else {
                    toIndex = carList.size() - 1;//直接定位到结尾
                }
                List<GPCar> childCarList = carList.subList(startIndex, toIndex);//如果tags集合大于40条数据就就截取前35条
                System.out.println("listSize:" + childCarList.size());
                jsonObject.put("data", childCarList);
            } else {
                jsonObject.put("data", carList);
            }
            jsonObject.put("count", carList.size());
            jsonObject.put("curr", curr);
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/coachFindCarList")
    public ResponseEntity<HashMap<String, Object>> coachFindCarList(String ChargeMan, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();


            List<GPCar> carList = gpCarService.selectCarByCoach(ChargeMan);
            System.out.println("listSize:" + carList.size());
            if (!carList.isEmpty()) {
                System.out.println("开始写入json");
                System.out.println(jsonObject.toJSONString());
                jsonObject.put("data", carList);
                System.out.println("写入json完成");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.ok().build();
            } else {
                //userList为空的情况
                System.out.println("carList为空！");
                jsonObject.put("message", "未能查询到相关信息！");
                writer.write(jsonObject.toJSONString());
                writer.close();
                return ResponseEntity.notFound().build();
            }
    }

    @RequestMapping(value = "/addCar")
    public ResponseEntity<HashMap<String, Object>> addCar(GPCar car, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();

        int i = gpCarService.insertCar(car);
        if (i > 0) {
            jsonObject.put("status", "Success");
            jsonObject.put("message", "用户添加成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "用户添加失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        System.out.println("车辆添加成功");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/deleteCar")
    public ResponseEntity<HashMap<String, Object>> deleteCar(Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备删除："+id);

        try {
            gpCarService.delCarById(id);//交由service层进行删除工作

            System.out.println(id + "  删除完成");
            jsonObject.put("message", "Success");
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            System.out.println(id + "删除失败");
            jsonObject.put("error", exception.toString());
            System.out.println("写入json完成");
            writer.write(jsonObject.toJSONString());
            writer.close();
            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/selectCarById")
    public ResponseEntity<HashMap<String, Object>> selectCarById(Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("单独查询："+id);
        //进行修改前，对用户的详细信息进行查看
        GPCar gpCar = gpCarService.selectCarById(id);

        jsonObject.put("status", "SUCCESS");
        jsonObject.put("carInfo", gpCar);
        writer.write(jsonObject.toJSONString());
        writer.close();

        return ResponseEntity.ok().build();
    }
    //修改车辆信息
    @RequestMapping(value = "/updateCar")
    public ResponseEntity<HashMap<String, Object>> updateCar(Integer id,String VIN, String CarNumber, String Brand, Integer Status, String ChargeMan, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        System.out.println("准备开始修改："+id);

        int result = gpCarService.updateCarById(id, VIN, CarNumber, Brand, Status,ChargeMan);
        System.out.println(result);
        if (result == 1) {
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("message", "车辆信息修改成功！");
        } else {
            jsonObject.put("status", "Fail");
            jsonObject.put("message", "车辆信息修改失败！");
        }
        writer.write(jsonObject.toJSONString());
        writer.close();
        return ResponseEntity.ok().build();
    }
}
