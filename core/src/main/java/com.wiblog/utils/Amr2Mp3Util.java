//package com.wiblog.utils;
//
//import java.io.File;
//
//import it.sauronsoftware.jave.AudioAttributes;
//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncodingAttributes;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * TODO 描述
// *
// * @author pwm
// * @date 2019/7/30
// */
//@Slf4j
//public class Amr2Mp3Util {
//
//    /**
//     *  
//     *      * 将amr格式转为wav格式 
//     *      * @param amrFilePath amr文件 
//     *      * @return  wav文件路径 
//     *      
//     */
//    public static void convertAmr2Mp3(File source) {
//        File target = new File(source.getPath().split("\\.")[0]+".wav");
//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec("pcm_s16le");
//        audio.setBitRate(new Integer(256));
//        audio.setChannels(new Integer(1));
//        audio.setSamplingRate(new Integer(16000));
//        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("wav");
//        attrs.setAudioAttributes(audio);
//        Encoder encoder = new Encoder();
//        try {
//            encoder.encode(source, target, attrs);
//        } catch (Exception e) {
//            log.info(e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        String sourcePath = "E:\\桌面\\nTwCM4kuS2VZ7UpMgt1LLaFmCJyvbphUNow4fJG0ej1QKS3ZQtn7SQnrqapa9EMO.amr";
//        File source = new File(sourcePath);
//        convertAmr2Mp3(source);
//
//    }
//}
