let vm;
let app = new Vue({
    el: "#app",
    data: {
        uid:Number(window.location.pathname.slice(6))/12345,
        user: {},
        activeName: "first",
        userActiveName: "first",
        userSettingActiveName:"first",
        // 侧边栏
        sidebarActive: "message",
        // 我的回复
        userComment: [],
        userCommentPageNum: 1,
        userCommentPageSize: 10,
        userCommentTotal: 0,
        userCommentOrderBy: "desc",
        // 回复我的
        userReply: [],
        userReplyPageNum: 1,
        userReplyPageSize: 10,
        userReplyTotal: 0,
        userReplyOrderBy: "desc",
        // 绑定列表
        bindList: {
            "phone":false,
            "email":false,
            "github":false,
            "weibo":false
        },
        // 邮箱
        emailVisible: false,
        emailInput: "",
        emailCode: "",
        emailCodeDisable: true,
        emailCodeInputDisable:true,
        errorEmailMsg: "",
        errorEmailCodeMsg: "",
        emailCodeBtnMsg: "",
    },
    beforeCreate() {
        vm = this;
    },
    mounted() {
        let msg = Cookies.get("error");
        if (msg !== undefined && msg !== ""){
            this.sidebarActive = "user";
            this.$message({message: msg, type: "error"});
            Cookies.remove("error");
        }

        this.getUserInfo();

        this.getBinding();
    },
    methods: {
        // 获取用户基础信息
        getUserInfo(){
            $.get("/u/info",{id:this.uid},function (res) {
               if (res.code === 10000){
                   vm.user=res.data;
                   vm.getUserComment(this.userCommentOrderBy);
                   vm.getUserReply(this.userReplyOrderBy);
               }
            });
        },
        // 我的回复
        getUserComment(orderBy) {
            this.userCommentOrderBy = orderBy;
            $.get("/comment/getUserComment", {
                uid:vm.uid,
                type:"comment",
                pageNum: vm.userCommentPageNum,
                pageSize: vm.userCommentPageSize,
                orderBy: orderBy
            }, function (res) {
                if (res.code === 10000) {
                    vm.userComment = res.data.records;
                    vm.userCommentTotal = res.data.total;
                }
            });
        },
        // 我的评论
        getUserReply(orderBy) {
            this.userReplyOrderBy = orderBy;
            $.get("/comment/getUserComment", {
                uid:vm.uid,
                type:"type",
                pageNum: vm.userReplyPageNum,
                pageSize: vm.userReplyPageSize,
                orderBy: orderBy
            }, function (res) {
                if (res.code === 10000) {
                    vm.userReply = res.data.records;
                    vm.userReplyTotal = res.data.total;
                }
            });
        },
        // 我的回复换页
        userCommentHandlePageNum(pageNum) {
            this.userCommentPageNum = pageNum;
            this.getUserComment(this.userCommentOrderBy);
        },
        //回复我的换页
        userReplyHandlePageNum(pageNum) {
            this.userReplyPageNum = pageNum;
            this.getUserReply(this.userReplyOrderBy);
        },
        // 绑定状态
        getBinding() {
            $.get("/u/getBindingList", function (res) {
                if (res.code === 10000) {
                    $.each(res.data, function (index, item) {

                        vm.bindList[item.identityType] = item.identifier;
                    });
                    console.log(vm.bindList);
                }
            })
        },
        // 检测邮箱
        checkEmail() {
            $.post('/u/checkEmail', {value: this.emailInput}, function (res) {
                if (res.code === 10000) {
                    vm.errorEmailMsg = '';
                    vm.emailCodeDisable = false;
                } else {
                    vm.errorEmailMsg = res.msg;
                    vm.emailCodeDisable = true;
                }
            });
        },
        // 获取邮件验证码
        getEmailCode() {
            let time = 60;
            this.emailCodeBtn = true;
            this.emailCodeInputDisable=false;
            $.get("/getEmailCheckCode", {email: this.emailInput}, function (res) {
                if (res.code === 10000) {
                    vm.$message({message: "发送验证码成功，请登录邮箱查看", type: "success"});
                }
            });
            setInterval(function () {
                if (time === 0) {
                    vm.emailCodeBtn = false;
                    vm.emailCodeBtnMsg = "获取验证码";
                    return;
                    //clearInterval();
                }
                vm.emailCodeBtnMsg = time + "s";
                time--;
            }, 1000)
        },
        mailCancel() {
            this.emailInput = "";
            this.emailCode = "";
            this.emailCodeDisable = true;
            this.errorEmailMsg = "";
            this.errorEmailCodeMsg = "";
            this.emailCodeBtnMsg = "获取验证码";
        },
        mailEnter(){
            if (this.emailInput.trim() === "") {
                vm.$message.error("请输入邮箱");
            }else if(this.emailCode.trim() === ""){
                vm.$message.error("请输入验证码");
            }
            $.post("/u/binding",{type:"email",val:this.emailInput,code:this.emailCode},function (res) {
                if (res.code === 10000){
                    vm.getBinding();
                    vm.$message({message: "绑定邮箱成功", type: "success"});
                    vm.emailVisible=false;
                }else {
                    vm.emailCodeBtnMsg=res.msg;
                }
            });
        },
        bingGithub(){
            Cookies.set('back',window.location.href,{ expires: 1, path: '/' });
            window.location.href="https://github.com/login/oauth/authorize?client_id=9d543dc4501558c6759f&redirect_uri=https://127.0.0.1/u/github/callback&response_type=code&state=banding";
        },
        unBinding(type){
            this.$confirm('是否取消绑定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                $.post("/u/unBinding",{type:type},function (res) {
                    if (res.code === 10000){
                        vm.$message({message: "解绑成功", type: "success"});
                        vm.getBinding();
                    }
                });
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

            var result = "";
            // 同一年不显示年份
            if (new Date().getFullYear() !== year) {
                result += year + "."
            }
            return result + month + "." + day + " " + hour + ":" + minute;
        }
    }

});