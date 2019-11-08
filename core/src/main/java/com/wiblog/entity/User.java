package com.wiblog.entity;


import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 用户表
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
    private Long uid;


    /**
     * 用户名
     */
    private String username;

    /**
     * 性别 male or female
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatarImg;

    /**
     * 状态 0删除
     */
    private Boolean state;

    /**
     * 创建时间
     */
    private Date createTime;

}
