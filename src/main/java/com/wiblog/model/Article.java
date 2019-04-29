package com.wiblog.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pwm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable{
    private static final long serialVersionUID = 1724537271232331195L;

    private Integer id;

    private String author;

    private String title;

    private String content;

    private String articleSummary;

    private String tags;

    private String articleCategories;

    private String url;

    private String articleUrl;

    private Integer likes;

    private Integer hits;

    private Integer commentsCounts;

    private Date createTime;

    private Date updateTime;

}