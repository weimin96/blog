(function () {
    'use strict';

    var app = new Vue({
        el: "#app",
        data:{
            tagList: '',
            time: '',
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

                console.log(user);
                if (user !== null){
                    $("#reply_body").removeClass("no-login");
                    $("#reply_avatar").attr("src",user.avatarImg);
                    app.uid = user.id;
                }
            },
            reply(){
                $.post("/comment/reply",{content:app.replyData.content,uid: app.uid})
            }
        }
    });

}());