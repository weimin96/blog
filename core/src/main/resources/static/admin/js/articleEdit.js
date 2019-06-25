'use strict';

var app = new Vue({
    el: "#app",
    data: {
        categoryList: [],
        // selected: '',
        // tag: '',
        tagList: [],
        isChangeTag: false,
        articleId: '',
        article:{
            title: '',
            content: '',
            articleCategories: '',
            articleSummary: '',
            tags: ''
        }
    },
    created(){
        this.getCategory();
        this.initData();
    },
    methods: {
        getCategory() {
            $.get("/category/getCategory",function (data) {

                if (data.code === 10000){
                    app.categoryList = data.data;
                    return app.categoryList;
                }
            })
        },
        submitTag: function () {
            // console.log(app.article.tags);
            this.tagList = this.article.tags.slice().split(/[\n\s+,，]/g);
            this.isChangeTag = false;
        },
        changeTag: function () {
            this.article.tags = this.tagList.toString();
            this.isChangeTag = true;
        },
        initData: function () {
            this.articleId = window.location.search.substr(4);
            let id = this.articleId;
            if (id === ''){
                return;
            }
            $.get("/post/get/"+id,function (res) {
                if (res.code === 10000){
                    app.article.title=res.data.title;
                    app.article.content=res.data.content;
                    app.article.articleCategories=res.data.articleCategories;
                    app.article.articleSummary=res.data.articleSummary;
                    app.article.tags=res.data.tags;
                    app.tagList = app.article.tags.slice().split(/[\n\s+,，]/g);
                }
            });
        },
        // 文章发表
        pushArticle: function () {
            // 发表新文章
            if (this.articleId === ''){
                $.post('/post/push',{
                    title: this.article.title,
                    author: 'areo',
                    content: this.article.content,
                    tags: this.article.tags,
                    articleCategories: this.article.articleCategories,
                    url: this.article.url,
                    articleSummary: this.article.articleSummary,
                    likes: 0,
                    hits: 0,
                    commentsCounts: 0
                })
            }
        }

    }
});
