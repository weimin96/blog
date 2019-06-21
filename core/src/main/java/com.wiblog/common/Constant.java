package com.wiblog.common;

/**
 * 常量类
 * @author pwm
 * @date 2019/4/25
 */
public class Constant {

    /**
     * 邮箱格式
     */
    public static final String EM = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 手机号格式
     */
    public static final String PH = "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,5-9]))\\d{8}$";

    /**
     * 非特殊字符
     */
    public static final String NON_SPECIAL_CHAR = "^[a-zA-Z0-9_\\u4e00-\\u9fa5\\s·]+$";
}
