package com.example.contorller;

import com.example.entity.VerifyCode;
import com.example.service.IVerifyCodeGen;
import com.example.service.impl.SimpleCharVerifyCodeGenImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class VerifyCodeController {

    @GetMapping("/verifyCode")//验证吗生成接口
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            //LOGGER.info(code);//这里的code就是正确的验证码
            //将VerifyCode绑定session
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            //LOGGER.info("", e);
            e.printStackTrace();
        }
    }

    @GetMapping("/checkVerifyCode")//验证码验证接口
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");//用户输入的输入框
        System.out.println(code);
        // 验证验证码
        String sessionCode = request.getSession().getAttribute("VerifyCode").toString();
        if (code != null && !"".equals(code) && sessionCode != null && !"".equals(sessionCode)) {
            if (code.equalsIgnoreCase(sessionCode)) {
                response.getWriter().println("验证通过！");
                System.out.println("验证通过！");
            } else {
                response.getWriter().println("验证失败！");
                System.out.println("验证失败！");
            }
        } else {
            response.getWriter().println("验证失败！");
            System.out.println("验证失败！");
        }
    }

}
