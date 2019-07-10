package com.wiblog.interceptor;

import com.wiblog.entity.User;
import com.wiblog.utils.IPUtil;
import com.wiblog.utils.WiblogUtil;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义拦截器
 *
 * @author pwm
 * @date 2019/7/1
 */
@Component
@Slf4j
public class BaseInterceptor implements HandlerInterceptor {
    //    @Resource
//    private Commons commons;

    private static final String USER_AGENT = "user-agent";
/*
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();

        log.info("UserAgent:" + request.getHeader(USER_AGENT));
        log.info("用户访问地址:{}, 来路地址: {}" ,uri, IPUtil.getIpAddr(request));


        //请求拦截处理
        User user = WiblogUtil.getLoginUser(request);
        if (null == user) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                //这里还是有安全隐患,cookie是可以伪造的
                user = userService.queryUserById(uid);
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        //设置get请求的token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }*/

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 一些工具类和公共方法
//        log.info("拦截");
//        httpServletRequest.setAttribute("commons", commons);
    }
}
