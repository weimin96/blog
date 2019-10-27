package com.wiblog.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 分页 查询
     * @param title title
     * @param content content
     * @param pageable pageable
     * @return Page
     */
    Page<EsArticle> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
