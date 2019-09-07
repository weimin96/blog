(function () {
    'use strict';

    let app = new Vue({
        el: "#app",
        data:{
            tagList: '',
            time: '',
            articleId: '',
            uid: '',
            replyData:{
                content: ''
            }
        },

        created(){
            this.initData();
        },
        methods: {
            initData(){
                let tags = document.getElementById("tags").value;
                this.tagList = tags.slice().split(/[\n\s+,，]/g);

                let createTime = document.getElementById("createTime").value;
                this.time = dateFormat2(createTime);

                let article_id = document.getElementById("articleId").value;
                this.articleId = article_id;

                // 获取评论
                $.post("/comment/commentListPage",{articleId:article_id},function(res){
                    if(res.code === 10000){

                    }else{
                        app.$message.error("获取评论失败");
                    }
                });

                // 评论框
                if (user !== null){
                    $("#reply_body").removeClass("no-login");
                    $("#reply_avatar").attr("src",user.avatarImg);
                    this.uid = user.uid;
                }


            },
            reply(){
                if(this.replyData.content.trim() === ""){
                    app.$message.error("请输入评论内容");
                    return;
                }
                $.post("/comment/reply",{content:app.replyData.content,uid: app.uid,articleId:app.articleId},
                    function (res) {
                        if(res.code === 10000){
                            app.$message({message:"评论成功",type:"success"});
                        }else{
                            app.$message.error(res.msg);
                        }
                    });
            }
        }
    });

}());