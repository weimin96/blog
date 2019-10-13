var vm;
var app = new Vue({
    el: "#app",
    data: {
        searchKey: "",
        tableData: [],
        pageNum: 1,
        pageSize: 10,
        total: 0
    },
    beforeCreate(){
      vm = this;
    },
    created() {
        this.init();
    },
    methods: {
        init: function () {
            $.post("/post/articles", {pageSize: this.pageSize, pageNum: this.pageNum}, function (res) {
                if (res.code === 10000) {
                    vm.tableData = res.data.records;
                    vm.total = res.data.total;
                }
            });

        },
        formatterInfo: function (row, column) {
            return "评论" + row.commentsCounts + "-喜欢" + row.likes + "-点击" + row.hits;
        },
        formatterDate: function (row, column) {
            let date = new Date(row.createTime);
            let year = date.getFullYear();
            let month = change(date.getMonth());
            let day = change(date.getDate());

            function change(t) {
                if (t < 10) {
                    return "0" + t;
                } else {
                    return t;
                }
            }
            return year + "/" + month + "/" + day;
        },
        handleView: function (index,row) {
            top.location.href=row.articleUrl;
        },
        handleEdit: function (index,row) {
            window.location.href="/admin/articleEdit?id="+row.id;
        },
        handleDelete: function (index,row) {
            this.$message({message:"删除成功",type: 'success'});
        },
        handlePageNum: function (val) {
            this.pageNum=val;
            this.init();
        },
        // 跳转到分类页面
        gotoCategoryPage: function (url) {
            top.location.href="/"+url;
        }
    }
});