package com.lczyfz.edp.springboot.demo.sys.utils;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author YRW
 * @Date 2022/10/6
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@Component
public class EmailUtil {

    public static Boolean emailSend(String email, String contents) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.qq.com");

        mailSender.setUsername("2528236370@qq.com");

        mailSender.setPassword("dpzassdbwugudhhb");

        mailSender.setPort(465);

        mailSender.setProtocol("smtps");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("2528236370@qq.com");

        message.setTo(email);

        message.setSubject("试卷批阅提醒");

        message.setText(contents);

        try {
            mailSender.send(message);
        } catch (MailException ignored) {
            return false;
        }

        return true;
    }
}
