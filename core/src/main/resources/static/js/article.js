(function () {
    'use strict';

    Vue.component('article-comment', {
        props: {
            "user_avatar": String,
            "replyContent": String,
            "reply": Function,
            "nologin": Boolean
        },
        methods: {},
        template:
            '<div class="comment-avatar">' +
            '<img class="pull-left" v-bind:src="user_avatar">' +
            '</div>' +
            '<div id="reply_body" class="comment-item-body reply-body" v-bind:class="{nologin:nologin,showreply:item[id] != null}">' +
            '<div class="no-reply-msg">请先<a class="btn-login main-background" href="../../login">登录</a>后发表评论 (・ω・)' +
            '</div>' +
            '<div class="reply-textarea">' +
            '<textarea cols="80" name="msg" rows="5" v-on:keyup.enter="reply" v-model.trim="replyContent" placeholder="请自觉遵守互联网相关的政策法规，严禁发布色情、暴力、反动的言论。"></textarea>' +
            '</div>' +
            '<button type="submit" class="comment-submit main-background" @click="reply">发表评论</button>' +
            '<div class="comment-emoji"><i class="fa fa-smile-o"></i><span>表情</span></div>' +
            '</div>'
    });

    let app = new Vue({
        el: "#app",
        data: {
            nologin: true,
            tagList: '',
            author: '',
            // 文章发表时间
            articleTime: '',
            articleId: '',
            //评论文章文本内容
            replyContent: '',
            // 评论用户文本内容
            replyUserContent: '',
            // 评论用户头像
            user_avatar: "/img/reply-avatar.svg",
            // 评论排序
            orderBy: 'asc',
            commentList: [],
            // 是否有评论
            isHasComment: false,
            // 格式化评论时间
            now: "",
            commentTime: [60 * 60 * 3 * 1000, 60 * 60 * 2 * 1000, 60 * 60 * 1000, 60 * 30 * 1000, 60 * 10 * 1000, 60 * 5 * 1000],
            commentTimeStr: ["三小时前", "两小时前", "一小时前", "半小时前", "10分钟前", "刚刚"],
            showReplyIndex: -1,
            // 父评论id
            parentId: 0
        },
        created() {
            this.initData();
        },
        methods: {
            initData: function () {
                var vm = this;
                let tags = document.getElementById("tags").value;
                this.tagList = tags.slice().split(/[\n\s+,，]/g);

                let createTime = document.getElementById("createTime").value;
                this.articleTime = dateFormat2(createTime);

                let article_id = document.getElementById("articleId").value;
                this.articleId = article_id;

                let author = document.getElementById("author").value;
                this.author = author;

                // 获取评论
                this.getComment("asc");

                // 评论框
                if (user !== null) {
                    this.nologin = false;
                    if(user.avatarImg !== ""){
                        this.reply_avatar = user.avatarImg;
                    }
                }


            },
            // 评论排序
            getComment: function(orderBy){
                this.orderBy = orderBy;
                var vm = this;
                $.post("/comment/commentListPage", {articleId: this.articleId,orderBy:this.orderBy}, function (res) {
                    if (res.code === 10000) {
                        vm.commentList = res.data.data.records;
                        vm.now = res.data.time;
                        if (vm.commentList.length > 0) {
                            vm.isHasComment = true;
                        }
                    } else {
                        vm.$message.error("获取评论失败");
                    }
                });
            },
            // 回复评论
            replyUser: function (commentId) {
                var vm = this;
                if (this.replyUserContent.trim() === "") {
                    this.$message.error("请输入评论内容");
                    return;
                }
                $.post("/comment/reply", {
                        content: this.replyUserContent,
                        articleId: this.articleId,
                        parentId: vm.parentId,
                        genId: commentId
                    },
                    function (res) {
                        if (res.code === 10000) {
                            vm.$message({message: "评论成功", type: "success"});
                            window.location.reload();
                        } else {
                            vm.$message.error(res.msg);
                        }
                    });
            },
            // 回复文章
            replyArticle: function () {
                var vm = this;
                if (this.replyContent.trim() === "") {
                    this.$message.error("请输入评论内容");
                    return;
                }
                $.post("/comment/reply", {content: this.replyContent, articleId: this.articleId},
                    function (res) {
                        if (res.code === 10000) {
                            vm.$message({message: "评论成功", type: "success"});
                            window.location.reload();
                        } else {
                            vm.$message.error(res.msg);
                        }
                    });
            },
            // 回复评论框显示
            showReplyBtn: function (index,parentId) {
                this.showReplyIndex = this.showReplyIndex === index ? -1 : index;
                // 楼中楼回复时设置父评论id
                this.parentId = parentId;
            }
        },
        filters: {
            // 用于评论的时间格式化
            commentDateFormat: function (d) {
                var date = new Date(d);
                var year = date.getFullYear();
                var month = change(date.getMonth());
                var day = change(date.getDate());
                var hour = change(date.getHours());
                var minute = change(date.getMinutes());

                function change(t) {
                    if (t < 10) {
                        return "0" + t;
                    } else {
                        return t;
                    }
                }

                var result = "";
                $.each(app.commentTime, function (index, item) {
                    if (app.now - item > date.getTime()) {
                        return null;
                    } else {
                        result = app.commentTimeStr[index];
                    }
                });
                // 返回数组内的时间文字
                if (result !== "") {
                    return result;
                }
                // 同一年不显示年份
                if (new Date(app.now).getFullYear() !== year) {
                    result += year + "."
                }
                return result + month + "." + day + " " + hour + ":" + minute;
            }
        }
    });


}());