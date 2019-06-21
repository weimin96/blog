package com.wiblog.controller;


import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 *  控制层
 *
 * @author pwm
 * @date 2019-06-01
 */
@RestController
@RequestMapping("/u")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ServerResponse<User> login(String account, String password, HttpServletRequest request) {
        ServerResponse response = userService.login(account, password);
        if (!response.isSuccess()) {
            return response;
        }
        // TODO redis缓存 cookies 登录日志
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
