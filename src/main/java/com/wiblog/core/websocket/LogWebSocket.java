package com.wiblog.core.websocket;

import com.alibaba.fastjson.JSON;
import com.wiblog.core.common.Constant;
import com.wiblog.core.entity.User;
import com.wiblog.core.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * @author pwm
 * @date 2019/12/17
 */
@ServerEndpoint(value = "/websocket/log")
@Component
@Slf4j
public class LogWebSocket {

    private ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("cache-pool-%d")
            .daemon(true).build();
    private ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 200L,
            TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory);

    private Process process;
    private InputStream inputStream;

    private IUserRoleService userRoleService = (IUserRoleService) ContextLoader.getCurrentWebApplicationContext();

    private RedisTemplate redisTemplate = (RedisTemplate) ContextLoader.getCurrentWebApplicationContext();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 整个会话
     */
    private HttpSession httpSession;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        log.info("连接");
    }

    /**
     * 异步线程 发送消息
     *
     * @param in      in
     * @param session session
     */
    private void tailLogThread(InputStream in, Session session) {
        executorService.execute(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                    session.getBasicRemote().sendText(line + "<br>");
                }
            } catch (IOException e) {
                log.error("异常", e);
            }
        });

    }

    @OnMessage
    public void onMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            String userJson = (String) redisTemplate.opsForValue().get(Constant.LOGIN_REDIS_KEY + message);
            if (StringUtils.isNotBlank(userJson)) {
                User user = JSON.parseObject(userJson, User.class);
                if (userRoleService.checkAuthorize(user, 2).isSuccess()) {
                    log.info("鉴权成功");
                    try {
//            process = Runtime.getRuntime().exec("tail -f /home/pwm/log/log.log");
                        process = Runtime.getRuntime().exec("cmd /c powershell Get-Content E:\\桌面\\log.log -Wait");

                        inputStream = process.getInputStream();
                        // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
                        tailLogThread(inputStream, session);
                        return;
                    } catch (IOException e) {
                        log.error("异常", e);
                    }
                }
            }
        }
        log.info("鉴权失败");
        onClose();
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
