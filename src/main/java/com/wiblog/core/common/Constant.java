package com.wiblog.core.common;

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

    public static final String LOGIN_REDIS_KEY = "user_login_";

    public static final String COOKIES_KEY = "uToken";

    /**
     * redis key 前缀 邮箱验证码
     */
    public static final String CHECK_EMAIL_KEY = "check_email_";

    public static final String VERIFY_CODE_SESSION_KEY = "verify_code";

    /**
     * 点赞redis key
     */
    public static final String LIKE_RECORD_KEY = "like_record_";

    /**
     * 邮箱发送次数
     */
    public static final String EMAIL_COUNT = "email_count_";

    /**
     * 淘宝ip地址库
     */
    public static final String IP_TAOBAO_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    /**
     * 分类列表缓存key
     */
    public static final String CATEGORY_KEY = "category";
}

