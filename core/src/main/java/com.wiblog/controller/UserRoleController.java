package com.wiblog.controller;


import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;
import com.wiblog.service.IUserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 *  控制层
 *
 * @author pwm
 * @date 2019-10-09
 */
@RestController
@RequestMapping("/user-role")
public class UserRoleController extends BaseController{

    private IUserRoleService userRoleService;

    @Autowired
    public UserRoleController(IUserRoleService userRoleService){
        this.userRoleService = userRoleService;
    }

    /**
     * 超级管理员分配权限
     * @param request request
     * @param uid 用户id
     * @param roleId 角色id
     * @return ServerResponse
     */
    public ServerResponse assignPermission(HttpServletRequest request,Long uid,Long roleId){
        // 是否超级管理员
        if(!isSupAdmin(request)){
            return ServerResponse.error("没有权限",90002);
        }
        User user = getLoginUser(request);
        return userRoleService.assignPermission(user,uid,roleId);
    }

    /**
     * 获取某个用户权限
     * @param request request
     * @param uid uid
     * @return ServerResponse
     */
    public ServerResponse getUserRole(HttpServletRequest request,Long uid){
        // 是否超级管理员
        if(!isSupAdmin(request)){
            return ServerResponse.error("没有权限",90002);
        }
        return userRoleService.getUserRole(uid);
    }
}
