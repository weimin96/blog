package com.wiblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;
import com.wiblog.mapper.UserRoleMapper;
import com.wiblog.service.IUserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-10-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public ServerResponse assignPermission(User user, Long uid, Long id) {
        if(user == null){
            return ServerResponse.error("用户未登录",90001);
        }
        // 是否为超级管理员
        int count = userRoleMapper.selectCount(new QueryWrapper<UserRole>().eq("uid",user.getUid()).eq("role_id",1));
        if(count <= 0){
            return ServerResponse.error("没有权限",90002);
        }
        // 分配权限
        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        userRole.setRoleId(id);
        count = userRoleMapper.insert(userRole);
        if (count <=0){
            return ServerResponse.error("权限分配失败",90003);
        }
        return ServerResponse.success(null,"权限分配成功");
    }
}
