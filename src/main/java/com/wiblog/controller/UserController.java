package com.wiblog.controller;

import com.wiblog.common.ServerResponse;
import com.wiblog.model.User;
import com.wiblog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pwm
 * @date 2019/4/16
 */
@RestController("/u")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ServerResponse<User> login(@RequestParam("account") String account,
                                      @RequestParam("password") String password){
        return userService.login(account,password);
    }

    @PostMapping("/register")
    public ServerResponse register(User user, HttpServletRequest request){
        return userService.register(user);
    }

    @PostMapping("/checkUsername")
    public ServerResponse checkUsername(String value){
        return userService.checkUsername(value);
    }

    @PostMapping("/checkPhone")
    public ServerResponse checkPhone(String value){
        return userService.checkPhone(value);
    }

    @PostMapping("/checkEmail")
    public ServerResponse checkEmail(String value){
        return userService.checkEmail(value);
    }
}
