package com.wiblog.controller;

import com.wiblog.aop.RequestRequire;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Comment;
import com.wiblog.service.ICommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

/**
 * 评论控制层
 *
 * @author pwm
 * @date 2019/9/1
 */
@Log
@RestController
@RequestMapping("/comment")
public class CommentController {

    private ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 发表评论
     *
     * @param comment comment
     * @return ServerResponse
     */
    @PostMapping("/reply")
    @RequestRequire(require = "uid,articleId,content", parameter = Comment.class)
    public ServerResponse reply(Comment comment) {
        return commentService.reply(comment);
    }

    @PostMapping("/commentListPage")
    public ServerResponse commentListPage(
            Long articleId,
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        if (articleId == null){
            return ServerResponse.error("参数错误",300001);
        }
        return commentService.commentListPage(articleId, pageNum, pageSize,orderBy);
    }
}