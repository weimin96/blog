package com.wiblog.vo;

import com.wiblog.entity.Comment;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/9/1
 */
@Data
public class CommentVo implements Serializable{

    private static final long serialVersionUID = -4046504102997611516L;

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
     * 回复的评论id 0为评论文章
     */
    private Long answererId;

    /**
     * 点赞数量
     */
    private Integer likes;

    /**
     * 评论内容
     */
    private String content;

    private Date createTime;

    private Date updateTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarImg;


}
