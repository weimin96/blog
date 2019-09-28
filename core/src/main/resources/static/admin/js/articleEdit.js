'use strict';

var mavonEditor = window['mavon-editor'];
Vue.use(mavonEditor);
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
        article:{
            title: '',
            content: '',
            articleCategories: '',
            articleSummary: '',
            tags: '',
            url: ''
        },
        // 富文本编辑工具栏
        toolbars: {
            bold: true, // 粗体
            italic: true, // 斜体
            quote: true, // 引用
            ol: true, // 有序列表
            ul: true, // 无序列表
            link: true, // 链接
            imagelink: true, // 图片链接
            code: true, // code
            table: true, // 表格
            undo: true, // 上一步
            redo: true, // 下一步
            preview: true,
            navigation: true,// 导航目录
            fullscreen: true, // 全屏编辑
            readmodel: true, // 沉浸式阅读
            htmlcode: true, // 展示html源码
        },
        // 抽屉
        drawer: false,
        direction: 'rtl',
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
        // 文章发表修改
        pushArticle: function () {
            // 发表新文章
            if (this.articleId === ''){
                $.post('/post/push',{
                    title: this.article.title,
                    content: this.article.content,
                    tags: this.article.tags,
                    articleCategories: this.article.articleCategories,
                    url: this.article.url,
                    articleSummary: this.article.articleSummary
                },function (res) {
                    if (res.code === 10000){
                        window.parent.location.href = window.location.protocol+"//"+window.location.host+res.data;
                    }else{
                        alert(res.msg);
                    }
                })
            }else {
                $.post('/post/update',{
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
        $imgAdd(pos, $file){
            // 第一步.将图片上传到服务器.
            var formdata = new FormData();
            formdata.append('image', $file);
            axios({
                url: 'server url',
                method: 'post',
                data: formdata,
                headers: { 'Content-Type': 'multipart/form-data' },
            }).then((url) => {
                // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
                // $vm.$img2Url 详情见本页末尾
                $vm.$img2Url(pos, url);
            })
        },
        // 点击显示标签编辑框
        showTagInput: function () {
            this.isShowTagInput = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        // 标签输入完成
        handleInputConfirm() {
            let tagTemp = this.tagTemp;
            if (tagTemp) {
                this.tagList.push(tagTemp);
            }
            this.isShowTagInput = false;
            this.tagTemp = '';
        },
        // 删除标签
        handleClose(tag) {
            this.tagList.splice(this.tagList.indexOf(tag), 1);
        },

    }
});
