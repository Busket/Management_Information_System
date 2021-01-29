package com.example.service.impl;

import com.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMimeMail(String to, String subject, String content) {//发送一般的邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(content, true);
            mailSender.send(message);
            System.out.println("邮件已经发送。");
        } catch (MessagingException e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }

    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {//发送带有附件的邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try{
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(content, true);

            FileSystemResource fs = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, fs);

            mailSender.send(message);
        }catch (MessagingException e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMailMessage(String to, String subject, String content) {//测试用的方法
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("今晚开会");
            message.setText("你好呀小游游！\n给你看个东西");
            message.setTo(to);
            message.setFrom("gzqzzr44@163.com");
            mailSender.send(message);
            System.out.println("确认邮件已发送！");
    }

}
