package com.wiblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.Constant;
import com.wiblog.common.ResultConstant;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserAuth;
import com.wiblog.exception.WiblogException;
import com.wiblog.mapper.UserAuthMapper;
import com.wiblog.mapper.UserMapper;
import com.wiblog.service.IUserService;
import com.wiblog.utils.Md5Util;
import com.wiblog.utils.WiblogUtil;
import com.wiblog.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author pwm
 * @since 2019-06-01
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ServerResponse<User> login(String account, String password) {

        //非空判断
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new WiblogException("用户名和密码不能为空");
        }
        account = account.trim();
        password = password.trim();
        // 用户名 邮箱 手机号
        UserAuth userAuth = new UserAuth();
        if (account.matches(Constant.EM)) {

            userAuth.setIdentityType("email");
        } else if (account.matches(Constant.PH)) {
            userAuth.setIdentityType("phone");
        } else {
            userAuth.setIdentityType("username");
        }
        password = Md5Util.MD5(password);
        userAuth.setPassword(password);
        userAuth.setIdentifier(account);
        User user = userAuthMapper.login(userAuth);
        if (user == null) {
            throw new WiblogException("用户名或密码错误");
        }
        return ServerResponse.success(user, ResultConstant.UserCenter.LOGIN_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String username, String phone, String email, String password) {

        // 校验用户名
        checkUsername(username);

        // 插入用户表
        User user = new User();
        user.setUsername(username);
        user.setAvatarImg("https://wwww.wiblog.cn/img/reply-avatar.svg");
        userMapper.insertReturnId(user);
        Long uid = user.getUid();

        // 校验密码
        if (StringUtils.isNotBlank(password)) {
            password = password.trim();
            if (password.length() < 6) {
                throw new WiblogException("密码长度必须大于6位");
            }
        } else {
            throw new WiblogException("密码长度必须大于6位");
        }
        password = Md5Util.MD5(password);

        // 校验手机号
        if (StringUtils.isNotBlank(phone)) {
            checkPhone(phone);
            // 校验成功
            UserAuth userAuth = new UserAuth();
            userAuth.setUid(uid);
            userAuth.setIdentityType("phone");
            userAuth.setIdentifier(phone);
            userAuth.setPassword(password);
            userAuth.setCreateTime(new Date());
            userAuthMapper.insert(userAuth);
        }
        // 校验邮箱
        if (!StringUtils.isBlank(email)) {
            checkEmail(email);
            // 校验成功
            UserAuth userAuth = new UserAuth();
            userAuth.setUid(uid);
            userAuth.setIdentityType("email");
            userAuth.setIdentifier(email);
            userAuth.setPassword(password);
            userAuth.setCreateTime(new Date());
            userAuthMapper.insert(userAuth);
        }

        // 校验成功
        UserAuth userAuth = new UserAuth();
        userAuth.setUid(uid);
        userAuth.setIdentityType("username");
        userAuth.setIdentifier(username);
        userAuth.setPassword(password);
        userAuth.setCreateTime(new Date());
        userAuthMapper.insert(userAuth);
    }

    @Override
    public void checkUsername(String username) {
        // 非空校验
        if (StringUtils.isBlank(username)) {
            throw new WiblogException("用户名不能为空");
        }
        //不能带特殊字符
        if (!username.matches(Constant.NON_SPECIAL_CHAR)) {
            throw new WiblogException(ResultConstant.UserCenter.USERNAME_ERROR_MSG);
        }
        // 用户名长度必须大于4个字符且小于32字符
        if (username.length() < 4 || username.length() > 32) {
            throw new WiblogException(ResultConstant.UserCenter.USERNAME_LEN_ERROR_MSG);
        }
        //用户名已存在 只校验本地帐户
        int checkUsername = userAuthMapper.checkUnique("username", username);
        if (checkUsername > 0) {
            throw new WiblogException(ResultConstant.UserCenter.USERNAME_EXIT_MSG);
        }
    }

    @Override
    public void checkPhone(String phone) {
        // 手机号格式校验
        if (!phone.matches(Constant.PH)) {
            throw new WiblogException(ResultConstant.UserCenter.PHONE_ERROR_MSG);
        }
        //手机号已存在
        int checkPhone = userAuthMapper.checkUnique("phone", phone);
        if (checkPhone > 0) {
            throw new WiblogException(ResultConstant.UserCenter.PHONE_EXIT_MSG);
        }
    }

    @Override
    public void checkEmail(String email) {
        // 邮箱格式校验
        if (!email.matches(Constant.EM)) {
            throw new WiblogException(ResultConstant.UserCenter.EMAIL_ERROR_MSG);
        }
        //邮箱已存在
        int checkEmail = userAuthMapper.checkUnique("email", email);
        if (checkEmail > 0) {
            throw new WiblogException(ResultConstant.UserCenter.EMAIL_EXIT_MSG);
        }
    }

    @Override
    public User loginUser(HttpServletRequest request) {
        String token = WiblogUtil.getCookie(request, Constant.COOKIES_KEY);
        if (StringUtils.isNotBlank(token)) {
            String userJson = (String) redisTemplate.opsForValue().get(Constant.LOGIN_REDIS_KEY + token);
            if (StringUtils.isNotBlank(userJson)) {
                return JSON.parseObject(userJson, User.class);
            }
        }
        return null;
    }

    @Override
    public ServerResponse getAllUsername() {
        List<Map<String, String>> list = userMapper.selectUsername();
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse userManageListPage(Integer state, String username, Integer pageNum, Integer pageSize, String orderBy) {
        Page<UserVo> page = new Page<>(pageNum, pageSize);
        if ("asc".equals(orderBy)) {
            page.setAsc("create_time");
        } else {
            page.setDesc("create_time");
        }
        IPage<UserVo> iPage = userMapper.selectUserManagePage(page, state, username);
        return ServerResponse.success(iPage, "获取用户列表成功");
    }

    @Override
    public ServerResponse getBindingList(Long uid) {
        List<UserAuth> list = userAuthMapper.selectList(new QueryWrapper<UserAuth>().eq("uid",uid).eq("state",1));
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse binding(Long uid, String type, String val, String code) {


        if ("email".equals(type)){
            // 校验 验证码
            String checkCode = (String) redisTemplate.opsForValue().get(Constant.CHECK_EMAIL_KEY + val);
            if (StringUtils.isBlank(val) || !val.equals(checkCode)){
                return ServerResponse.error("验证码错误",30001);
            }
        }else if("phone".equals(type)){
            return null;
        }else{
            return ServerResponse.error("类型错误",30001);
        }

        // 已经绑定过
        List<UserAuth> userAuthList = userAuthMapper.selectList(new QueryWrapper<UserAuth>().eq("uid",uid).eq("state",1));
        for (UserAuth item:userAuthList){
            if (type.equals(item.getIdentityType())){
                return ServerResponse.error("已经绑定过了",30001);
            }
        }
        // 插入
        UserAuth userAuth = userAuthList.get(0);
        userAuth.setCreateTime(new Date());
        userAuth.setIdentifier(val);
        userAuth.setIdentityType(type);
        userAuthMapper.insert(userAuth);
        return ServerResponse.success(null,"绑定成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteUser(Long uid) {
        userMapper.updateStateToZero(uid);
        userAuthMapper.updateStateToZero(uid,null);
        return ServerResponse.success(null,"注销成功");
    }

    @Override
    public ServerResponse unBinding(Long uid, String type) {
        userAuthMapper.updateStateToZero(uid,type);
        return ServerResponse.success("解绑成功");
    }
}
