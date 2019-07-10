package com.wiblog.controller;


import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.exception.WiblogException;
import com.wiblog.service.IUserService;
import com.wiblog.utils.Md5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;


/**
 *  控制层
 *
 * @author pwm
 * @date 2019-06-01
 */
@RestController
@RequestMapping("/u")
@Transactional(rollbackFor = WiblogException.class)
@Slf4j
public class UserController extends BaseController{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ServerResponse<User> login(String account, String password, HttpServletRequest request) {
        // 错误次数
        Integer errorCount = cache.get("login_error_count");
        ServerResponse response;
        try {
            response = userService.login(account, password);
        }catch (Exception e){
            if (errorCount == null){
                errorCount = 1;
            }else {
                errorCount++;
            }
            if (errorCount > 3) {
                return ServerResponse.error("您输入密码已经错误超过3次，请10分钟后尝试",10005);
            }
            cache.set("login_error_count", errorCount, 10 * 60);
            String msg = "登录失败";
            if (e instanceof WiblogException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return ServerResponse.error(msg,10004);
        }

        // TODO redis缓存 cookies 登录日志
//        String token = Md5Util.MD5(request.getSession().getId() + user.getId().toString());
        return response;
    }

    @PostMapping("/register")
    public ServerResponse register(User user) {
        return userService.register(user);
    }

    @PostMapping("/checkUsername")
    public ServerResponse checkUsername(String value) {
        return userService.checkUsername(value);
    }

    @PostMapping("/checkPhone")
    public ServerResponse checkPhone(String value) {
        return userService.checkPhone(value);
    }

    @PostMapping("/checkEmail")
    public ServerResponse checkEmail(String value) {
        return userService.checkEmail(value);
    }
}
