package com.wiblog.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.core.common.Constant;
import com.wiblog.core.common.ServerResponse;
import com.wiblog.core.entity.Article;
import com.wiblog.core.entity.User;
import com.wiblog.core.mapper.ArticleMapper;
import com.wiblog.core.service.IArticleService;
import com.wiblog.core.service.IUserRoleService;
import com.wiblog.core.vo.ArticleDetailVo;
import com.wiblog.core.vo.ArticlePageVo;
import com.wiblog.core.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-06-12
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ServerResponse getAllArticle() {
        List<Map<String,String>> list =  articleMapper.selectAllArticle();
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse<IPage> articlePageList(Integer pageNum, Integer pageSize,Long categoryId) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        IPage<ArticlePageVo> iPage = articleMapper.selectPageList(page,0,categoryId);
        return ServerResponse.success(iPage,"获取文章列表成功");
    }

    @Override
    public ServerResponse<IPage> articlesManage(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        IPage<ArticlePageVo> iPage = articleMapper.selectPageList(page,null,null);
        return ServerResponse.success(iPage,"获取文章列表成功");
    }

    @Override
    public ServerResponse delArticle(Long id) {
        if (id == null){
            return ServerResponse.success(null,"删除失败");
        }
        int count = articleMapper.updateState(id);
        ArticleVo article = articleMapper.selectArticleById(id);
        String title = "";
        if (article != null){
            title = article.getTitle();
        }
        // 删除排行榜数据
        Long result = redisTemplate.opsForZSet().remove(Constant.ARTICLE_RANKING_KEY,String.valueOf(id));
        // 删除点击率 点赞
        redisTemplate.opsForHash().delete(Constant.HIT_RECORD_KEY,String.valueOf(id));
        redisTemplate.opsForHash().delete(Constant.LIKE_RECORD_KEY,String.valueOf(id));
        return ServerResponse.success(result,"删除成功",title);
    }

    @Override
    public ServerResponse getArticleRank(Set<Object> rankSet) {
        List<Article> result = new ArrayList<>();
        List<Long> articleIds = new ArrayList<>();
        if (rankSet != null && rankSet.size()>0){
            for (Object o : rankSet) {
                articleIds.add(Long.parseLong(o.toString()));
            }
            result = articleMapper.selectArticleByIds(articleIds);
        }
        return ServerResponse.success(result);
    }

    @Override
    public ServerResponse getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        return ServerResponse.success(article,"获取文章成功");
    }

    @Override
    public ServerResponse<ArticleDetailVo> getArticle(String url, User user) {
        ArticleDetailVo detailVo = articleMapper.selectArticleByUrl(url);
        if (detailVo == null){
            return ServerResponse.error("获取文章失败",20001);
        }
//        detailVo.setContent(WiblogUtil.mdToHtml(detailVo.getContent()));
        if (detailVo.getPrivately() && !userRoleService.checkAuthorize(user,2).isSuccess()){
            return ServerResponse.error("获取文章失败",20001);
        }
        return ServerResponse.success(detailVo,"获取文章成功");
    }


}
