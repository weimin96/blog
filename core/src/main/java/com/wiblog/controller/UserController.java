package com.wiblog.controller;


import com.alibaba.fastjson.JSON;
import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.common.Constant;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.exception.WiblogException;
import com.wiblog.service.IUserService;
import com.wiblog.thirdparty.GithubProvider;
import com.wiblog.utils.Md5Util;
import com.wiblog.utils.WiblogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 控制层
 *
 * @author pwm
 * @date 2019-06-01
 */
@RestController
@RequestMapping("/u")
@Slf4j
@Api(tags = "用户中心api")
public class UserController extends BaseController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "登录", notes = "根据账号密码登录，账号可以为手机号、用户名、邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form")
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
            redisTemplate.opsForValue().set(Constant.LOGIN_REDIS_KEY + token, JSON.toJSONString(user),7,TimeUnit.DAYS);
            // cookies
            WiblogUtil.setCookie(response, token);
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
                log.warn(msg, e);
            }
            return ServerResponse.error(msg, 10004);
        }
        return serverResponse;
    }

    @GetMapping("/logout")
    public ServerResponse logout(HttpServletRequest request,HttpServletResponse response){
        String token = WiblogUtil.getCookie(request,Constant.COOKIES_KEY);
        if (StringUtils.isNotBlank(token)){
            WiblogUtil.delCookie(request,response);
            Boolean bool = redisTemplate.delete(Constant.LOGIN_REDIS_KEY + token);
            log.info("退出登录{}",bool);
        }

        return ServerResponse.success(null,"退出成功");
    }

    @ApiOperation(value = "注册", notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "email", value = "邮箱(非必填)", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号(非必填)", paramType = "form")
    })
    @PostMapping("/register")
    public ServerResponse register(String username, String phone, String email, String password) {
        try {
            userService.register(username, phone, email, password);
        } catch (Exception e) {
            String msg = "注册失败";
            if (e instanceof WiblogException) {
                msg = e.getMessage();
            } else {
                log.warn(msg, e);
            }
            return ServerResponse.error(msg, 30001);
        }
        return ServerResponse.success(null);
    }

    @ApiOperation(value = "检查用户名")
    @PostMapping("/checkUsername")
    public ServerResponse checkUsername(String value) {
        try {
            userService.checkUsername(value);
        } catch (WiblogException e) {
            log.warn(e.getMessage(), e);
            return ServerResponse.error(e.getMessage(), 30001);
        }
        return ServerResponse.success("用户名校验成功");
    }

    @ApiOperation(value = "检查手机号")
    @PostMapping("/checkPhone")
    public ServerResponse checkPhone(String value) {
        try {
            userService.checkPhone(value);
        } catch (WiblogException e) {
            log.warn(e.getMessage(), e);
            return ServerResponse.error(e.getMessage(), 30001);
        }
        return ServerResponse.success("手机号校验成功");
    }

    @ApiOperation(value = "检查邮箱")
    @PostMapping("/checkEmail")
    public ServerResponse checkEmail(String value) {
        try {
            userService.checkEmail(value);
        } catch (WiblogException e) {
            log.warn(e.getMessage(), e);
            return ServerResponse.error(e.getMessage(), 30001);
        }
        return ServerResponse.success("手机号校验成功");
    }

    @GetMapping("/getAllUsername")
    public ServerResponse getAllUsername() {
        return userService.getAllUsername();
    }

    /**
     * 获取用户管理列表
     *
     * @param state    用户状态
     * @param username 用户名模糊查询
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param orderBy  orderBy
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
        return userService.userManageListPage(state, username, pageNum, pageSize, orderBy);
    }

    @PostMapping("/deleteUser")
    public ServerResponse deleteUser(HttpServletRequest request){
        User user = getLoginUser(request);
        if (user == null){
            return ServerResponse.error("用户未登录",30001);
        }
        return userService.deleteUser(user.getUid());
    }

    /**
     * github 登录回调
     * @param request request
     * @param response response
     * @param code code
     */
    @GetMapping("/github/callback")
    public ServerResponse githubLogin(HttpServletRequest request, HttpServletResponse response, String code,String state) throws IOException {
        String accessToken = githubProvider.getAccessToken(code,state);
        Map githubUser = githubProvider.getUser(accessToken);
        if ("login".equals(state)){
            User user = githubProvider.registerGithub(githubUser,accessToken);
            // redis缓存
            String token = Md5Util.MD5(request.getSession().getId() + user.getUid().toString());
            redisTemplate.opsForValue().set(Constant.LOGIN_REDIS_KEY + token, JSON.toJSONString(user),7, TimeUnit.DAYS);
            // cookies
            WiblogUtil.setCookie(response, token);
            // 跳转历史页面
            String url = WiblogUtil.getCookie(request,"back");
            log.info(url);
            response.sendRedirect(url);
            return null;
        }else {
            User user = getLoginUser(request);
            if (user != null){
                return githubProvider.bingGithub(user.getUid(),githubUser,accessToken);
            }
            return ServerResponse.error("用户未登录",30001);
        }
    }


    @GetMapping("/getBindingList")
    public ServerResponse getBindingList(HttpServletRequest request){
        User user = getLoginUser(request);
        if (user != null){
            return userService.getBindingList(user.getUid());
        }
        return ServerResponse.error("用户未登录",30001);
    }

    /**
     * 绑定手机号或邮箱
     * @param request request
     * @param type type
     * @param val val
     * @param code code
     * @return ServerResponse
     */
    @PostMapping("/binding")
    public ServerResponse binding(HttpServletRequest request,String type,String val,Integer code){
        User user = getLoginUser(request);
        if (user != null){
            return userService.binding(user.getUid(),type,val,code);
        }
        return ServerResponse.error("用户未登录",30001);
    }

    /**
     * 绑解手机号或邮箱
     * @param request request
     * @param type type
     * @return ServerResponse
     */
    @PostMapping("/unBinding")
    public ServerResponse unBinding(HttpServletRequest request,String type){
        User user = getLoginUser(request);
        if (user != null){
            return userService.unBinding(user.getUid(),type);
        }
        return ServerResponse.error("用户未登录",30001);
    }
}
