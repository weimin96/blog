package com.wiblog.service;

import com.wiblog.common.ServerResponse;
import com.wiblog.model.User;

/**
 * @author pwm
 * @date 2019/4/16
 */
public interface UserService {
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
}
