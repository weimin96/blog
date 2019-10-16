package com.wiblog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-10-16
 */
@Data
public class Picture implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;


    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件链接
     */
    private String url;

    /**
     * 额外字段
     */
    private String extra;

    private Date createTime;

    private Date updateTime;

}
