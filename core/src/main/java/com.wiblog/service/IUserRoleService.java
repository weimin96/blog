package com.wiblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;

/**
 *   服务类
 *
 * @author pwm
 * @since 2019-10-09
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 超级管理员分配权限
     * @param user 登录用户
     * @param uid uid
     * @param id 权限id
     * @return ServerResponse
     */
    ServerResponse assignPermission(User user, Long uid, Long id);

    /**
     * 获取某个用户权限
     * @param uid uid
     * @return ServerResponse
     */
    ServerResponse getUserRole(Long uid);
}
