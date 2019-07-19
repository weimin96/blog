package com.wiblog.entity;



import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-06-01
 */
@Data
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long uid;


    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别 male or female
     */
    private String sex;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarImg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次登录时间
     */
    private Date logged;

}
