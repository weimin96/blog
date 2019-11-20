package com.wiblog.core.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/10/25
 */
public interface EsArticleRepository extends ElasticsearchRepository<EsArticle,String> {
    /**
     * 删除
     * @param articleId articleId
     */
    void deleteByArticleId(Long articleId);
}
