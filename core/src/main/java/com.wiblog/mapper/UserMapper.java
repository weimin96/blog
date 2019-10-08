package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.User;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取所有用户名
     * @return List
     */
    List<Map<String, String>> selectUsername();
}
