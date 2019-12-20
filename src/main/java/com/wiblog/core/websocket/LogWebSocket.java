package com.wiblog.core.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author pwm
 * @date 2019/12/17
 */
@ServerEndpoint("/websocket/log")
@Component
@Slf4j
public class LogWebSocket {

    private Process process;
    private InputStream inputStream;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("连接");
        try {
            process = Runtime.getRuntime().exec("tail -f /home/pwm/log/log.log");
//            process = Runtime.getRuntime().exec("cmd /c powershell Get-Content E:\\桌面\\log.log -Wait");

            inputStream = process.getInputStream();
            //inputStream = new FileInputStream(new File("E:\\桌面\\log.log"));
            // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
            tailLogThread(inputStream, session);
        } catch (IOException e) {
            log.error("异常",e);
        }
    }

    /**
     * 异步线程 发送消息
     *
     * @param in      in
     * @param session session
     */
//    @Async
    public void tailLogThread(InputStream in, Session session) {
        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                    session.getBasicRemote().sendText(line + "<br>");
                }
            } catch (IOException e) {
                log.error("异常",e);
            }
        }).start();

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("关闭socket");
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 发生错误
     *
     * @param error e
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket错误", error);
    }
}
