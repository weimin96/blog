package com.wiblog.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 文档类 article
 *
 * @author pwm
 * @date 2019/10/25
 */
@Document(indexName = "article",type = "article")
public class EsArticle  implements Serializable {
    private static final long serialVersionUID = -7577471814974481136L;

    /**
     * id为String
     */
    @Id
    private String id;

    private String title;

    private String content;

    private String url;

    public EsArticle() {
    }

    public EsArticle(String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
