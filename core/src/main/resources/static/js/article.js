(function () {
    'use strict';

    var app = new Vue({
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
                var tags = document.getElementById("tags").value;
                this.tagList = tags.slice().split(/[\n\s+,，]/g);

                var createTime = document.getElementById("createTime").value;
                this.time = dateFormat2(createTime);

                var article_id = document.getElementById("articleId").value;
                this.articleId = article_id;

                console.log(user);
                if (user !== null){
                    $("#reply_body").removeClass("no-login");
                    $("#reply_avatar").attr("src",user.avatarImg);
                    app.uid = user.id;
                }
            },
            reply(){
                $.post("/comment/reply",{content:app.replyData.content,uid: app.uid,articleId:app.articleId},
                    function (res) {
                        if(res.code === 10000){
                            console.log("评论成功");
                        }else{

                        }
                    });
            }
        }
    });

}());