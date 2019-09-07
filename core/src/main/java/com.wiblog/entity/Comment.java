package com.wiblog.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 
 *
 * @author pwm
 * @date 2019-09-01
 */
@Data
public class Comment implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;


    /**
     * 用户id
     */
    private Long uid;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 父评论id 0为评论文章
     */
    private Long parentId;

    /**
     * 主评论id 0为评论文章
     */
    private Long genId;

    /**
     * 点赞数量
     */
    private Integer likes;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态
     */
    private Integer state;

    private Date createTime;

    private Date updateTime;

}
