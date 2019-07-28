package com.wiblog.controller;

import com.wiblog.entity.Voice;
import com.wiblog.utils.CheckoutUtil;
import com.wiblog.utils.WeixinUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/25
 */
@Controller
public class WeixinController {

    @GetMapping("/tokenCheck")
    @ResponseBody
    public void tokenCheck(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter print;
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
            try {
                print = response.getWriter();
                print.write(echostr);
                print.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/weixin/sign")
    @ResponseBody
    public Map<String,String> sign(String url){
        return WeixinUtil.sign(url);
    }

    @GetMapping("/weixin/test")
    public String weixin(){
        return "test";
    }

    String appKey = "TjuEiD9LXno0Nq2a"; // "填写你的appkey";
    String id = "LTAIzfXmhjZCqU5E"; // "填写你在阿里云网站上的AccessKeyId";
    String secret = "ofZJmVzBbtATyZl576hFLkbFS2w2bT"; // "填写你在阿里云网站上的AccessKeySecret";

    @GetMapping("/recognize")
    public Voice recognize(String localId){
        SpeechRecognizerDemo demo = new SpeechRecognizerDemo(appKey, id, secret);
        // TODO 重要提示： 这里用一个本地文件来模拟发送实时流数据，实际使用时，用户可以从某处实时采集或接收语音流并发送到ASR服务端
        Voice voice = new Voice();
        String id = UUID.randomUUID().toString();
        voice.setId(id);
        SpeechRecognizerDemo.voices.put(id,voice);
//        demo.process(file, 16000, id);

        demo.shutdown();
        try {
            voice.getMsg();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return voice;
    }
}
