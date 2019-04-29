package com.wiblog.dao;

import com.wiblog.model.User;

/**
 * @author pwm
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    User selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User login(User user);

    int checkUsername(String username);

    int checkEmail(String email);

    int checkPhone(String phone);
}