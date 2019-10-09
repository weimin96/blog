//package com.wiblog.entity;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * TODO 描述
// *
// * @author pwm
// * @date 2019/7/28
// */
//public class Voice {
//    private String id;
//
//    private String msg;
//
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getMsg() throws InterruptedException {
//        latch.await();
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//        latch.countDown();
//    }
//}
