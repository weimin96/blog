package com.wiblog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-06-12
 */
@Data
public class Article implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;


    /**
     * 作者名
     */
    private String author;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private String tags;

    /**
     * 分类
     */
    private String articleCategories;

    /**
     * 文章封面
     */
    private String url;

    /**
     * 文章地址
     */
    private String articleUrl;

    /**
     * 简介
     */
    private String articleSummary;

    /**
     * 喜欢
     */
    private Integer likes;

    /**
     * 点击量
     */
    private Integer hits;

    /**
     * 评论数
     */
    private Integer commentsCounts;

    private Date createTime;

    private Date updateTime;

}
