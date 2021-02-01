package com.example.service;

import javax.mail.MessagingException;

public interface MailService {
    /**
     *  发送多媒体类型邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendMimeMail(String to, String subject, String content) throws MessagingException;

    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);

    void sendSimpleMailMessage(String to, String subject, String content);
}
