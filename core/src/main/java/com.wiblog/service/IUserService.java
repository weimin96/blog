package com.wiblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 *   服务类
 *
 * @author pwm
 * @since 2019-06-01
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     * @param account 账号
     * @param password 密码
     * @return ServerResponse<User>
     */
    ServerResponse<User> login(String account, String password);

    /**
     * 注册
     * @param user user
     * @return ServerResponse<User>
     */
    ServerResponse register(User user);

    /**
     * 校验用户名
     * @param username username
     * @return ServerResponse
     */
    ServerResponse checkUsername(String username);

    /**
     * 校验手机号
     * @param phone phone
     * @return ServerResponse
     */
    ServerResponse checkPhone(String phone);

    /**
     * 校验邮箱
     * @param email email
     * @return ServerResponse
     */
    ServerResponse checkEmail(String email);

    /**
     * 获取当前登录用户
     * @param request request
     * @return user
     */
    User loginUser(HttpServletRequest request);

    /**
     * 获取所有用户名
     * @return ServerResponse
     */
    ServerResponse getAllUsername();

    /**
     * 获取用户管理列表
     * @param state 用户状态
     * @param username 用户名模糊查询
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param orderBy orderBy
     * @return ServerResponse
     */
    ServerResponse userManageListPage(Integer state, String username, Integer pageNum, Integer pageSize, String orderBy);
}
