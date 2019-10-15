package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.entity.Article;
import com.wiblog.vo.ArticleDetailVo;
import com.wiblog.vo.ArticlePageVo;
import com.wiblog.vo.ArticleVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-06-12
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获取所有文章标题
     * @return List<String>
     */
    List<Map<String,String>> selectAllArticle();

    /**
     * 获取文章分页列表
     * @param page page
     * @param state 私密
     * @return IPage
     */
    IPage<ArticlePageVo> selectPageList(Page<Article> page,@Param(value = "state") Integer state);

    /**
     * 获取文章详细信息
     * @param id id
     * @return ArticleVo
     */
    ArticleVo selectArticleById(Long id);

    /**
     * 文章页获取文章所有信息
     * @param url url
     * @return ArticleDetailVo
     */
    ArticleDetailVo selectArticleByUrl(String url);
}
