'use strict';

var app = new Vue({
    el: "#app",
    data: {
        categoryList: [],
        // selected: '',
        tagTemp: '',
        tagList: [],
        // 是否显示标签编辑框
        isShowTagInput: false,
        articleId: '',
        article: {
            title: '',
            content: '',
            articleCategories: '',
            articleSummary: '',
            tags: '',
            url: ''
        },
        editor: {}
    },
    created() {
        this.initData();
        this.getCategory();
    },
    mounted(){
        if( this.articleId === ''){
            this.initEditor();
        }
    },
    methods: {
        // 初始化编辑器
        initEditor: function(md){
            this.editor = editormd("editor", {
                width: "100%",
                height: "420",
                syncScrolling: true ,
                path: "../lib/editor.md/lib/",
                markdown: md,
                imageUpload: true,
                imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                imageUploadURL: "/uploadImageForEditorMd",
                editorTheme : "neo",
                watch:false,
                toolbarIcons: function () {
                    return ["bold", "italic", "del", "h1", "h2", "h3", "h4", "h5", "h6", "quote", "hr", "list-ul", "list-ol", "|", "table", "code", "link", "image", "|", "undo", "redo", "search","goto-line", "watch", "preview", "fullscreen"]
                },
            });
        },
        // 初始化数据
        initData: function () {
            this.articleId = window.location.search.substr(4);
            var id = this.articleId;
            if (id === '') {
                return;
            }
            $.get("/post/get/" + id, function (res) {
                if (res.code === 10000) {
                    app.article.title = res.data.title;
                    app.article.content = res.data.content;
                    app.article.articleCategories = res.data.articleCategories;
                    app.article.articleSummary = res.data.articleSummary;
                    app.article.tags = res.data.tags;
                    app.tagList = app.article.tags.slice().split(/[\n\s+,，]/g);
                    app.initEditor(app.article.content);
                }
            });
        },
        getCategory: function () {
            $.get("/category/getCategory", function (data) {

                if (data.code === 10000) {
                    app.categoryList = data.data;
                    return app.categoryList;
                }
            })
        },
        // 文章发表修改
        pushArticle: function () {
            this.article.content = this.editor.getMarkdown();
            this.article.articleSummary = this.article.content.substr(0, 200);
            // 发表新文章
            if (this.articleId === '') {
                $.post('/post/push', {
                    title: this.article.title,
                    content: this.article.content,
                    tags: this.article.tags,
                    articleCategories: this.article.articleCategories,
                    url: this.article.url,
                    articleSummary: this.article.articleSummary
                }, function (res) {
                    if (res.code === 10000) {
                        window.parent.location.href = window.location.protocol + "//" + window.location.host + res.data;
                    } else {
                        alert(res.msg);
                    }
                })
            } else {
                $.post('/post/update', {
                    id: this.articleId,
                    title: this.article.title,
                    content: this.article.content,
                    tags: this.article.tags,
                    articleCategories: this.article.articleCategories,
                    url: this.article.url,
                    articleSummary: this.article.articleSummary
                })
            }
        },
        // 点击显示标签编辑框
        showTagInput: function () {
            this.isShowTagInput = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        // 标签输入完成
        handleInputConfirm: function () {
            var tagTemp = this.tagTemp;
            if (tagTemp) {
                this.tagList.push(tagTemp);
                this.article.tags = this.tagList.toString();
            }
            this.isShowTagInput = false;
            this.tagTemp = '';
        },
        // 删除标签
        handleClose: function (tag) {
            this.tagList.splice(this.tagList.indexOf(tag), 1);
        }
    }
});
