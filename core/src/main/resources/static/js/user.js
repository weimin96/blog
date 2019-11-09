let vm;
let app = new Vue({
    el: "#app",
    data: {
        user: {},
        activeName: "first",
        userComment: [],

    },
    beforeCreate() {
        vm = this;
    },
    mounted() {
        this.user = user;
        this.getUserComment();
    },
    methods: {
        getUserComment() {
            $.get("/comment/getUserComment", function (res) {
                if (res.code === 10000) {
                    vm.userComment = res.data.records;
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