package com.example.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.GPCar;
import com.example.entity.GPUser;
import com.example.service.GPCarService;
import com.example.service.GPUserService;
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
}
