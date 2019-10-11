package com.wiblog.controller;


import com.alibaba.fastjson.JSON;
import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.common.Constant;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.exception.WiblogException;
import com.wiblog.service.IUserService;
import com.wiblog.utils.Md5Util;
import com.wiblog.utils.WiblogUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * 控制层
 *
 * @author pwm
 * @date 2019-06-01
 */
@RestController
@RequestMapping("/u")
@Transactional(rollbackFor = WiblogException.class)
@Slf4j
@Api(tags = "用户中心api")
public class UserController extends BaseController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value="登录", notes="根据账号密码登录，账号可以为手机号、用户名、邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true,paramType="form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true,paramType="form")
    })
    @PostMapping("/login")
    public ServerResponse login(String account, String password, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("登录");
        // 错误次数
        Integer errorCount = cache.get("login_error_count");
        ServerResponse serverResponse;
        try {
            serverResponse = userService.login(account, password);
            User user = (User) serverResponse.getData();
            // redis缓存
            String token = Md5Util.MD5(request.getSession().getId() + user.getUid().toString());
            redisTemplate.opsForValue().set(Constant.LOGIN_REDIS_KEY + token, JSON.toJSONString(user));
            redisTemplate.expire(Constant.LOGIN_REDIS_KEY + token, 7, TimeUnit.DAYS);
            // cookies
            WiblogUtil.setCookie(response,token);
            // TODO 登录日志

        } catch (Exception e) {
            if (errorCount == null) {
                errorCount = 1;
            } else {
                errorCount++;
            }
            if (errorCount > 3) {
                return ServerResponse.error("您输入密码已经错误超过3次，请10分钟后尝试", 10005);
            }
            cache.set("login_error_count", errorCount, 10 * 60);
            String msg = "登录失败";
            if (e instanceof WiblogException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return ServerResponse.error(msg, 10004);
        }
        return serverResponse;
    }

    @ApiOperation(value="注册", notes="用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true,paramType="form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true,paramType="form"),
            @ApiImplicitParam(name = "email", value = "邮箱(非必填)",paramType="form"),
            @ApiImplicitParam(name = "phone", value = "手机号(非必填)",paramType="form")
    })
    @PostMapping("/register")
    public ServerResponse register(User user) {
        return userService.register(user);
    }

    @ApiOperation(value="检查用户名")
    @PostMapping("/checkUsername")
    public ServerResponse checkUsername(String value) {
        return userService.checkUsername(value);
    }

    @ApiOperation(value="检查手机号")
    @PostMapping("/checkPhone")
    public ServerResponse checkPhone(String value) {
        return userService.checkPhone(value);
    }

    @ApiOperation(value="检查邮箱")
    @PostMapping("/checkEmail")
    public ServerResponse checkEmail(String value) {
        return userService.checkEmail(value);
    }

    @GetMapping("/getAllUsername")
    public ServerResponse getAllUsername(){
        return userService.getAllUsername();
    }

    /**
     * 获取用户管理列表
     *
     * @param state 用户状态
     * @param username 用户名模糊查询
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param orderBy orderBy
     * @return ServerResponse
     */
    @AuthorizeCheck(grade = "2")
    @PostMapping("/userManageListPage")
    public ServerResponse userManageListPage(
            @RequestParam(value = "state", required = false) Integer state,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) {
        return userService.userManageListPage(state,username,pageNum, pageSize,orderBy);
    }
}
