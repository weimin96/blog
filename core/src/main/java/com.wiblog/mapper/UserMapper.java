package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.User;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-06-01
 */
public interface UserMapper extends BaseMapper<User> {
    User login(User user);

    int checkUsername(String username);

    int checkEmail(String email);

    int checkPhone(String phone);
}
