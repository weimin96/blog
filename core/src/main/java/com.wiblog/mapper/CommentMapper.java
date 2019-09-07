package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.vo.CommentVo;
import com.wiblog.vo.SubCommentVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-09-01
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 分页查找主评论
     * @param page page
     * @param articleId articleId
     * @return IPage
     */
    IPage<CommentVo> selectCommentPage(Page page, @Param("articleId") Long articleId);

    /**
     * 查找子评论
     * @param commentId commentId
     * @return IPage
     */
    List<SubCommentVo> selectSubCommentLimit(@Param("commentId") Long commentId);

}
