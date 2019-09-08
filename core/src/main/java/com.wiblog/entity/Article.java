package com.wiblog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-06-12
 */
@Data
@ApiModel(description= "文章信息")
public class Article implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "文章id")
    private Long id;


    /**
     * 作者名
     */
    @ApiModelProperty(value = "作者名")
    private String author;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String tags;

    /**
     * 分类
     */
    @ApiModelProperty(value = "分类")
    private String articleCategories;

    /**
     * 文章封面
     */
    @ApiModelProperty(value = "文章封面图片url")
    private String url;

    /**
     * 文章地址
     */
    @ApiModelProperty(value = "文章url地址")
    private String articleUrl;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String articleSummary;

    /**
     * 喜欢
     */
    @ApiModelProperty(value = "喜欢数")
    private Integer likes;

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量")
    private Integer hits;

    /**
     * 评论数
     */
    @ApiModelProperty(value = "评论数")
    private Integer commentsCounts;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
