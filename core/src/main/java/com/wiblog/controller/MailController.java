package com.wiblog.controller;

import com.wiblog.common.Constant;
import com.wiblog.common.ServerResponse;
import com.wiblog.service.impl.MailServiceImpl;
import com.wiblog.utils.VerifyCodeUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/11/4
 */
@RestController
public class MailController {

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/getCheckCode")
    public ServerResponse getCheckCode() {
        String to = "15602332711@163.com";
        int checkCode = new Random().nextInt(9000) + 1000;
        // 有效期1天
        redisTemplate.opsForValue().set(Constant.CHECK_EMAIL_KEY + to, checkCode, 1, TimeUnit.DAYS);
        StringBuilder sb = new StringBuilder();
        try (Reader reader = new InputStreamReader(new FileInputStream(ResourceUtils.getFile("classpath:mail.vm")))){
            int s = 0;
            while ((s = reader.read())!= -1){

                sb.append((char) s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = sb.toString();
        message=message.replace("[0]",to);
        message=message.replace("[1]",checkCode+"");
        mailService.sendSimpleMail(to, "请激活你的邮箱账号", message);
        return ServerResponse.success(null);
    }

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyCodeUtils.getRandomCode(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验验证码
     */
    @PostMapping("/checkVerify")
    public boolean checkVerify(String code, HttpSession session) {
        if (StringUtils.isNotBlank(code)) {
            String check = (String) session.getAttribute(Constant.VERIFY_CODE_SESSION_KEY);
            return code.equals(check);
        }
        return false;
    }
}
