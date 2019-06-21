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
            if (this.articleId === ''){
                
            }
        }

    }
});
