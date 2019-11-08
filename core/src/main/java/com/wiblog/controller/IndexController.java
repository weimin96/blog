package com.wiblog.controller;

import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.service.IMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/11/6
 */
@RestController
public class IndexController extends BaseController{

    @Autowired
    private IMessageService messageService;

    @GetMapping("/getMessageCount")
    public ServerResponse getMessageCount(HttpServletRequest request){
        User user = getLoginUser(request);
        if (user !=null){
            return messageService.getMessageCount(user.getUid());
        }
        return ServerResponse.error("用户未登录",30001);
    }
}
