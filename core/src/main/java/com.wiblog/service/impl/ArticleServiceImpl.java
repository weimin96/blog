package com.wiblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.entity.Article;
import com.wiblog.mapper.ArticleMapper;
import com.wiblog.service.IArticleService;

import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-06-12
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
