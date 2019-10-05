package com.wiblog.controller;

import com.wiblog.entity.User;
import com.wiblog.service.IUserService;
import com.wiblog.utils.MapCache;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础Controller
 *
 * @author pwm
 * @date 2019/7/3
 */
public abstract class BaseController {

    @Autowired
    private IUserService userService;

    protected MapCache cache = MapCache.single();

    /**
     * 获取登录用户信息
     * @param request request
     * @return User
     */
    public User getLoginUser(HttpServletRequest request){
        return userService.loginUser(request);
    }
}
