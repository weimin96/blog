package com.wiblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wiblog.common.Constant;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;
import com.wiblog.mapper.UserRoleMapper;
import com.wiblog.service.IUserService;
import com.wiblog.utils.MapCache;
import com.wiblog.utils.VerifyCodeUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基础Controller
 *
 * @author pwm
 * @date 2019/7/3
 */
public abstract class BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    protected MapCache cache = MapCache.single();

    /**
     * 获取登录用户信息
     * @param request request
     * @return User
     */
    public User getLoginUser(HttpServletRequest request){
        return userService.loginUser(request);
    }

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            //设置相应类型,告诉浏览器输出的内容为图片
            response.setContentType("image/jpeg");
            //设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyCodeUtils.getRandomCode(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验验证码
     */
    @PostMapping("/checkVerify")
    public boolean checkVerify(String code, HttpSession session) {
        if (StringUtils.isNotBlank(code)) {
            String check = (String) session.getAttribute(Constant.VERIFY_CODE_SESSION_KEY);
            return code.equals(check);
        }
        return false;
    }
}
