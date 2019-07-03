package com.wiblog.interceptor;

import com.wiblog.utils.Commons;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;

/**
 * 自定义拦截器
 *
 * @author pwm
 * @date 2019/7/1
 */
@Component
@Log
public class BaseInterceptor implements HandlerInterceptor {
    @Resource
    private Commons commons;

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 一些工具类和公共方法
        log.info("拦截");
        httpServletRequest.setAttribute("commons", commons);
}
}
