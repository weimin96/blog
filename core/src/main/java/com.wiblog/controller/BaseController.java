package com.wiblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;
import com.wiblog.mapper.UserRoleMapper;
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

    @Autowired
    private UserRoleMapper userRoleMapper;

    protected MapCache cache = MapCache.single();

    /**
     * 获取登录用户信息
     * @param request request
     * @return User
     */
    public User getLoginUser(HttpServletRequest request){
        return userService.loginUser(request);
    }

    /**
     * 是否是超级管理员
     * @param request request
     * @return boolean
     */
    public boolean isSupAdmin(HttpServletRequest request){
        User user = getLoginUser(request);
        if(user == null){
            return false;
        }
        // 是否为超级管理员
        int count = userRoleMapper.selectCount(new QueryWrapper<UserRole>().eq("uid",user.getUid()).eq("role_id",1));
        return count > 0;
    }
}
