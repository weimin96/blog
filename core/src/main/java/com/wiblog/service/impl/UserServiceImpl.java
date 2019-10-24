package com.wiblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.Constant;
import com.wiblog.common.ResultConstant;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.exception.WiblogException;
import com.wiblog.mapper.UserMapper;
import com.wiblog.service.IUserService;
import com.wiblog.utils.Md5Util;
import com.wiblog.utils.WiblogUtil;
import com.wiblog.vo.CommentManageVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-06-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ServerResponse<User> login(String account, String password) {

        //非空判断
        if (StringUtils.isBlank(account)|| StringUtils.isBlank(password)){
            throw new WiblogException("用户名和密码不能为空");
        }
        account=account.trim();
        password=password.trim();
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
            throw new WiblogException("用户名或密码错误");
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

        user.setAvatarImg("http://blog.wiblog.cn/img/reply-avatar.svg");
        user.setSex("male");
        user.setCreateTime(new Date());
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
            return ServerResponse.error("用户名不能为空",ResultConstant.PARA_ILLEGAL_CODE);
        }
        //不能带特殊字符
        if (!username.matches(Constant.NON_SPECIAL_CHAR)){
            return ServerResponse.error(ResultConstant.UserCenter.USERNAME_ERROR_MSG,ResultConstant.UserCenter.USERNAME_ERROR_CODE);
        }
        // 用户名长度必须大于4个字符且小于32字符
        if ( username.length()<4 || username.length()>32){
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
            return ServerResponse.success(null,"手机号为空");
        }
        // 手机号格式校验
        if (!phone.matches(Constant.PH)){
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
            return ServerResponse.success(null,"邮箱为空");
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

    @Override
    public User loginUser(HttpServletRequest request) {
        String token = WiblogUtil.getCookie(request,Constant.COOKIES_KEY);
        if (StringUtils.isNotBlank(token)) {
            String userJson = (String) redisTemplate.opsForValue().get(Constant.LOGIN_REDIS_KEY +token);
            if (StringUtils.isNotBlank(userJson)){
                return JSON.parseObject(userJson, User.class);
            }
        }
        return null;
    }

    @Override
    public ServerResponse getAllUsername() {
        List<Map<String,String>> list = userMapper.selectUsername();
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse userManageListPage(Integer state, String username, Integer pageNum, Integer pageSize, String orderBy) {
        Page<User> page = new Page<>(pageNum,pageSize);
        if("asc".equals(orderBy)){
            page.setAsc("create_time");
        }else{
            page.setDesc("create_time");
        }
        IPage<User> iPage = userMapper.selectUserManagePage(page,state,username);
        return ServerResponse.success(iPage,"获取用户列表成功");
    }
}
