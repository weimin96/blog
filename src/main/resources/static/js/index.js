'use strict';

let vm;
let app = new Vue({
    el: "#post-list",
    data: {
        articleList: [],
    },
    beforeCreate: function () {
        vm = this;
    },
    mounted() {
        this.initData(1);
    },
    methods: {
        initData(currentPage) {
            $.ajax({
                type: 'POST',
                url: '/post/articles',
                dataType: 'json',
                data: {
                    pageNum: currentPage,
                    pageSize: "5"
                },
                success: function (data) {
                    vm.articleList = data.data.records;
                    $("#pagination").paging({
                        pageNum: data.data['current'],//当前所在页码
                        pages: data.data['pages'],//总页数
                        callback: function (currentPage) {
                            vm.initData(currentPage);
                        }
                    });
                }
            });
        }
    },
    filters: {
        dateFormat: function (d) {
            var date = new Date(d);
            var year = date.getFullYear();
            var month = switchNum(date.getMonth());
            var day = change(date.getDate());

            function change(t) {
                if (t < 10) {
                    return "0" + t;
                } else {
                    return t;
                }
            }

            function switchNum(month) {
                var arry = ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"];
                return arry[Number(month)];
            }

            return month + "月" + day + ", " + year;
        }
    }
});





