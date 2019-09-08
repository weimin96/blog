package com.wiblog.entity;



import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-06-01
 */
@ApiModel(description= "用户信息")
@Data
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "用户id")
    private Long uid;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 性别 male or female
     */
    @ApiModelProperty(value = "性别")
    private String sex;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像url")
    private String avatarImg;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    /**
     * 上次登录时间
     */
    @ApiModelProperty(value = "上次登录时间")
    private Date logged;

}
