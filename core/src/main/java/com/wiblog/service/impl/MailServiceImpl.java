package com.wiblog.service.impl;

import com.wiblog.common.Constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/11/4
 */
@Service
public class MailServiceImpl {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void sendSimpleMail(String to,String title,String content){
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


    public boolean checkEmail(String email,String checkCode){
        String code = (String) redisTemplate.opsForValue().get(Constant.CHECK_EMAIL_KEY+email);
        return code != null && code.equals(checkCode);
    }


}
