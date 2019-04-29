package com.wiblog.service.impl;

import com.wiblog.common.Constant;
import com.wiblog.common.ResultConstant;
import com.wiblog.common.ServerResponse;
import com.wiblog.dao.UserMapper;
import com.wiblog.model.User;
import com.wiblog.service.UserService;
import com.wiblog.utils.Md5Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pwm
 * @date 2019/4/16
 */
@Service
public class UserServiceImpl implements UserService {



    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String account, String password) {
        account=account.trim();
        password=password.trim();
        //非空判断
        if (StringUtils.isEmpty(account)|| StringUtils.isEmpty(password)){
            return ServerResponse.error(ResultConstant.UserCenter.NOT_ALLOW_NULL_MSG,ResultConstant.UserCenter.NOT_ALLOW_NULL_CODE);
        }
        // 用户名 邮箱 手机号


        User user= new User();
        if (account.matches(Constant.EM)){
            user.setEmail(account);
        }else if (account.matches(Constant.PH)){
            user.setPhone(account);
        }else {
            user.setUsername(account);
        }
        password= Md5Util.MD5(password);
        user.setPassword(password);
        User loginUser = userMapper.login(user);
        if (loginUser == null){
            return ServerResponse.error(ResultConstant.UserCenter.MATCH_ERROR_MSG,ResultConstant.UserCenter.MATCH_ERROR_CODE);
        }
        return ServerResponse.success(loginUser,ResultConstant.UserCenter.LOGIN_SUCCESS);
    }

    @Override
    public ServerResponse register(User user) {
        // 校验用户名
        ServerResponse checkValid = checkUsername(user.getUsername());
        if (!checkValid.isSuccess()){
            return checkValid;
        }
        // 校验手机号
        if (!StringUtils.isBlank(user.getPhone())){
            checkValid=checkPhone(user.getPhone());
            if (!checkValid.isSuccess()){
                return checkValid;
            }
        }
        // 校验邮箱
        if (!StringUtils.isBlank(user.getEmail())){
            checkValid=checkPhone(user.getEmail());
            if (!checkValid.isSuccess()){
                return checkValid;
            }
        }
        user.setPassword(Md5Util.MD5(user.getPassword()));

        user.setAvatarImg("");
        int count = userMapper.insert(user);
        if (count <=0){
            return ServerResponse.error(ResultConstant.UserCenter.REGISTER_ERROR_MSG,ResultConstant.UserCenter.REGISTER_ERROR_CODE);
        }
        return ServerResponse.success(user,ResultConstant.UserCenter.REGISTER_SUCCESS);
    }

    @Override
    public ServerResponse checkUsername(String username) {
        // 非空校验
        if (StringUtils.isBlank(username)){
            return ServerResponse.error(ResultConstant.PARA_ILLEGAL_MSG,ResultConstant.PARA_ILLEGAL_CODE);
        }
        //不能带特殊字符
        if (!username.matches(Constant.NON_SPECIAL_CHAR)){
            return ServerResponse.error(ResultConstant.UserCenter.USERNAME_ERROR_MSG,ResultConstant.UserCenter.USERNAME_ERROR_CODE);
        }
        // 用户名长度必须大于6个字符且小于32字符
        if ( username.length()<6 || username.length()>32){
            return ServerResponse.error(ResultConstant.UserCenter.USERNAME_LEN_ERROR_MSG,ResultConstant.UserCenter.USERNAME_LEN_ERROR_CODE);
        }
        //用户名已存在
        int checkUsername = userMapper.checkUsername(username);
        if (checkUsername>0){
            return ServerResponse.error(ResultConstant.UserCenter.USERNAME_EXIT_MSG,ResultConstant.UserCenter.USERNAME_EXIT_CODE);
        }
        return ServerResponse.success(null,ResultConstant.PARA_SUCCESS_MSG);
    }

    @Override
    public ServerResponse checkPhone(String phone) {
        // 非空校验
        if (StringUtils.isBlank(phone)){
            return ServerResponse.error(ResultConstant.PARA_ILLEGAL_MSG,ResultConstant.PARA_ILLEGAL_CODE);
        }
        // 手机号格式校验
        if (!phone.matches(Constant.EM)){
            return ServerResponse.error(ResultConstant.UserCenter.PHONE_ERROR_MSG,ResultConstant.UserCenter.PHONE_ERROR_CODE);
        }
        //手机号已存在
        int checkPhone = userMapper.checkPhone(phone);
        if (checkPhone>0){
            return ServerResponse.error(ResultConstant.UserCenter.PHONE_EXIT_MSG,ResultConstant.UserCenter.PHONE_EXIT_CODE);
        }
        return ServerResponse.success(null,ResultConstant.PARA_SUCCESS_MSG);
    }

    @Override
    public ServerResponse checkEmail(String email) {
        // 非空校验
        if (StringUtils.isBlank(email)){
            return ServerResponse.error(ResultConstant.PARA_ILLEGAL_MSG,ResultConstant.PARA_ILLEGAL_CODE);
        }
        // 邮箱格式校验
        if (!email.matches(Constant.EM)){
            return ServerResponse.error(ResultConstant.UserCenter.EMAIL_ERROR_MSG,ResultConstant.UserCenter.EMAIL_ERROR_CODE);
        }
        //邮箱已存在
        int checkEmail = userMapper.checkEmail(email);
        if (checkEmail>0){
            return ServerResponse.error(ResultConstant.UserCenter.EMAIL_EXIT_MSG,ResultConstant.UserCenter.EMAIL_EXIT_CODE);
        }
        return ServerResponse.success(null,ResultConstant.PARA_SUCCESS_MSG);
    }
}
