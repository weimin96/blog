'use strict';

let vm;
let app = new Vue({
    el: "#app",
    data: {
        pageNum: 1,
        records: [],
        noMore: false,
        id: -1,
        timer: false,
    },
    beforeCreate() {
        vm = this;
    },
    mounted() {
        this.init();
    },
    methods: {
        init() {
            this.id=-1;
            this.records=[];
            this.pageNum=1;
            this.loadImg();
            this.onscroll();
        },
        disabled() {
            let isExit = $("#" + vm.id).length > 0;
            // 存在还有加载
            return isExit && this.noMore;
        },
        loadImg() {
            if (this.disabled()) {
                return;
            }
            vm.$http.get('/getImageList', {
                params: {
                    "pageNum": vm.pageNum,
                    "pageSize": 15
                }
            }).then(function (response) {
                var records = response.data.data.records;
                vm.records = vm.records.concat(records);
                vm.id = vm.records[vm.records.length - 1].id;
                vm.noMore = vm.records.length >= response.data.data.total;
                vm.pageNum++;
            })
        },
        // 监听滚动
        onscroll() {
            window.onscroll = function () {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
                //变量windowHeight是可视区的高度
                var windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
                //变量scrollHeight是滚动条的总高度
                var scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
                //滚动条到底部的条件
                if (scrollTop + windowHeight + 20 > scrollHeight) {
                    clearTimeout(vm.timer);
                    vm.timer = setTimeout(function () {
                        vm.loadImg();
                    }, 300);
                }
            };
        },
        //上传图片成功
        uploadSuccess(){
            console.log("上传成功");
            this.init();
        }
    },
    filters:{
        dateFormatter: function (d) {
            let date = new Date(d);
            let year=date.getFullYear();
            let month=change(date.getMonth()+1);
            let day=change(date.getDate());
            function change(t){
                if(t<10){
                    return "0"+t;
                }else{
                    return t;
                }
            }
            return year+"."+month+"."+day;
        }
    }
});