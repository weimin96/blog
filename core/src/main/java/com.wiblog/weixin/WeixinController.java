//package com.wiblog.weixin;
//
//import com.iflytek.cloud.speech.SpeechUtility;
//import com.wiblog.entity.Voice;
//import com.wiblog.utils.Amr2Mp3Util;
//import com.wiblog.utils.CheckoutUtil;
//import com.wiblog.utils.WeixinUtil;
//import com.wiblog.utils.WordFilterUtil;
//import com.wiblog.utils.XunFeiUtil;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * TODO 描述
// *
// * @author pwm
// * @date 2019/7/25
// */
//@Controller
//public class WeixinController {
//
//    @GetMapping("/tokenCheck")
//    @ResponseBody
//    public void tokenCheck(HttpServletRequest request, HttpServletResponse response) {
//        PrintWriter print;
//        // 微信加密签名
//        String signature = request.getParameter("signature");
//        // 时间戳
//        String timestamp = request.getParameter("timestamp");
//        // 随机数
//        String nonce = request.getParameter("nonce");
//        // 随机字符串
//        String echostr = request.getParameter("echostr");
//        if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
//            try {
//                print = response.getWriter();
//                print.write(echostr);
//                print.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @GetMapping("/weixin/sign")
//    @ResponseBody
//    public Map<String, String> sign(String url) {
//        return WeixinUtil.sign(url);
//    }
//
//    @Autowired
//    private WordFilterUtil mWordFilterUtil;
//
//    @GetMapping("/weixin/wordFilter")
//    @ResponseBody
//    public String wordFilter(String text) {
//        if (StringUtils.isBlank(text)) {
//            return "";
//        }
//        return mWordFilterUtil.automaticSelection(text);
//    }
//
//    @GetMapping("/weixin/test")
//    public String weixin() {
//        return "test";
//    }
//
//    String appKey = "TjuEiD9LXno0Nq2a"; // "填写你的appkey";
//    String id = "LTAIzfXmhjZCqU5E"; // "填写你在阿里云网站上的AccessKeyId";
//    String secret = "ofZJmVzBbtATyZl576hFLkbFS2w2bT"; // "填写你在阿里云网站上的AccessKeySecret";
//
//    @GetMapping("/getVoiceByAliYun")
//    @ResponseBody
//    public Voice getVoiceByAliYun(String mediaId) {
//        SpeechRecognizerDemo demo = new SpeechRecognizerDemo(appKey, id, secret);
//        // TODO 重要提示： 这里用一个本地文件来模拟发送实时流数据，实际使用时，用户可以从某处实时采集或接收语音流并发送到ASR服务端
//        Voice voice = new Voice();
//        String id = UUID.randomUUID().toString();
//        voice.setId(id);
//        SpeechRecognizerDemo.voices.put(id, voice);
//        try {
//            File file = WeixinUtil.getWeixinFile(mediaId);
//            Amr2Mp3Util.convertAmr2Mp3(file);
//            File targetFile = new File(file.getPath().split("\\.")[0] + ".wav");
//            while (true) {
//                if (targetFile.exists()) {
//                    demo.process(file, 16000, id);
//                    demo.shutdown();
//                    voice.getMsg();
//                    break;
//                }
//                Thread.sleep(100);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return voice;
//    }
//
//    private static final String APPID = "5d36d6d1";
//
//    @Autowired
//    private XunFeiUtil xunFeiUtil;
//
//    @GetMapping("/getVoiceByXunFei")
//    @ResponseBody
//    public Voice getVoiceByXunFei(String mediaId) {
//
//        Voice voice = new Voice();
//        String id = UUID.randomUUID().toString();
//        voice.setId(id);
//        XunFeiUtil.voices.put(id,voice);
//        try {
//            File file = WeixinUtil.getWeixinFile(mediaId);
//            Amr2Mp3Util.convertAmr2Mp3(file);
//            File targetFile = new File(file.getPath().split("\\.")[0] + ".wav");
//            while (true) {
//                if (targetFile.exists()) {
//                    SpeechUtility.createUtility("appid=" + APPID);
//                    xunFeiUtil.Recognize(targetFile,id);
//                    voice.getMsg();
//                    System.out.println(voice.getMsg());
//                    break;
//                }
//                Thread.sleep(100);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return voice;
//    }
//
//}
