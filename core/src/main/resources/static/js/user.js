let vm;
let app = new Vue({
    el: "#app",
    data: {
        user: {},
        activeName: "first",
        userActiveName:"first",
        // 侧边栏
        sidebarActive:"message",
        // 我的回复
        userComment: [],
        userCommentPageNum:1,
        userCommentPageSize:10,
        userCommentTotal:0,
        userCommentOrderBy:"desc",
        // 回复我的
        userReply: [],
        userReplyPageNum:1,
        userReplyPageSize:10,
        userReplyTotal:0,
        userReplyOrderBy:"desc",
        // 绑定列表
        bindList:{},
        // 邮箱
        emailVisible:false,
        emailInput:"",
        emailCode:"",
        emailCodeBtn:false,
        errorEmailMsg:"邮箱格式错误",
        errorEmailCodeMsg:"验证码错误"
    },
    beforeCreate() {
        vm = this;
    },
    mounted() {
        this.user = user;
        this.getUserComment(this.userCommentOrderBy);
        this.getUserReply(this.userReplyOrderBy);
        this.getBinding();
    },
    methods: {
        getUserComment(orderBy) {
            this.userCommentOrderBy = orderBy;
            $.get("/comment/getUserComment",{pageNum:this.userCommentPageNum,pageSize:this.userCommentPageSize,orderBy:orderBy}, function (res) {
                if (res.code === 10000) {
                    vm.userComment = res.data.records;
                    vm.userCommentTotal = res.data.total;
                }
            });
        },
        getUserReply(orderBy) {
            this.userReplyOrderBy = orderBy;
            $.get("/comment/getUserReply",{pageNum:this.userReplyPageNum,pageSize:this.userReplyPageSize,orderBy:orderBy}, function (res) {
                if (res.code === 10000) {
                    vm.userReply = res.data.records;
                    vm.userReplyTotal = res.data.total;
                }
            });
        },
        // 我的回复换页
        userCommentHandlePageNum(pageNum){
            this.userCommentPageNum=pageNum;
            this.getUserComment(this.userCommentOrderBy);
        },
        //回复我的换页
        userReplyHandlePageNum(pageNum){
            this.userReplyPageNum=pageNum;
            this.getUserReply(this.userReplyOrderBy);
        },
        // 绑定状态
        getBinding(){
            $.get("/u/getBindingList",function (res) {
                if (res.code === 10000){
                    $.each(res.data,function (index,item) {
                       vm.bindList[item.identityType]=true;
                    });
                    console.log(vm.bindList);
                }
            })
        },
        // 检测邮箱
        checkEmail(){
            $.post('/u/checkEmail', {value: this.emailInput}, function (res) {
                if (res.code === 10000) {
                    vm.errorEmailMsg = '';
                    vm.emailCodeBtn = true;
                } else {
                    vm.errorEmailMsg = res.msg;
                    vm.emailCodeBtn = false;
                }
            });
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
            var result="";
            // 同一年不显示年份
            if (new Date().getFullYear() !== year) {
                result += year + "."
            }
            return result + month + "." + day + " " + hour + ":" + minute;
        }
    }

});