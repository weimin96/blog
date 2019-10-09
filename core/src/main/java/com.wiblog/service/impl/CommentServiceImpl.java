package com.wiblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Comment;
import com.wiblog.mapper.ArticleMapper;
import com.wiblog.mapper.CommentMapper;
import com.wiblog.service.ICommentService;
import com.wiblog.vo.CommentManageVo;
import com.wiblog.vo.CommentVo;
import com.wiblog.vo.SubCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 服务实现类
 *
 * @author pwm
 * @since 2019-09-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ServerResponse reply(Comment comment) {
        Date date = new Date();
        comment.setId(null);
        comment.setLikes(0);
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        // 没有回复id是回复文章
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
            comment.setGenId(0L);
            // 查找主评论数量
            int floor = commentMapper.selectCount(new QueryWrapper<Comment>()
                    .eq("article_id",comment.getArticleId())
                    .eq("gen_id",0L));
            comment.setFloor(++floor);
        }
        int count = commentMapper.insert(comment);
        if (count <= 0) {
            return ServerResponse.error("评论失败", 40001);
        }
        articleMapper.updateCommentCount(comment.getArticleId());
        return ServerResponse.success(null, "评论成功");
    }

    @Override
    public ServerResponse commentListPage(Long id,Integer pageNum,Integer pageSize,String orderBy) {
        Page<CommentVo> page = new Page<>(pageNum,pageSize);
        if("asc".equals(orderBy)){
            page.setAsc("create_time");
        }else{
            page.setDesc("create_time");
        }
        IPage<CommentVo> commentIPage = commentMapper.selectCommentPage(page,id);
        for (CommentVo item:commentIPage.getRecords()){
            List<SubCommentVo> subComment = commentMapper.selectSubCommentLimit(item.getId(),orderBy);
            item.setSubCommentVoList(subComment);
        }
        Map<String,Object> result = new HashMap<>(16);
        result.put("data",commentIPage);
        result.put("time",System.currentTimeMillis());
        return ServerResponse.success(result,"获取评论成功");
    }

    @Override
    public ServerResponse commentManageListPage(Long articleId, String title, Integer state, String username, Integer pageNum, Integer pageSize, String orderBy) {
        Page<CommentManageVo> page = new Page<>(pageNum,pageSize);
        if("asc".equals(orderBy)){
            page.setAsc("create_time");
        }else{
            page.setDesc("create_time");
        }
        IPage<CommentManageVo> commentIPage = commentMapper.selectCommentManagePage(page,articleId,state,title,username);
        return ServerResponse.success(commentIPage,"获取评论成功");
    }

    @Override
    public ServerResponse deleteComment(Integer id) {
        int count = commentMapper.updateStateById(id);
        return ServerResponse.success(count,"删除评论成功");
    }

    @Override
    public ServerResponse restoreComment(Integer id) {
        int count = commentMapper.restoreStateById(id);
        return ServerResponse.success(count,"恢复删除评论成功");
    }
}
