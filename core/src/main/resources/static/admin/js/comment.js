var vm;
var app = new Vue({
    el: "#app",
    data: {
        searchKey: "",
        tableData: [],
        pageNum: 1,
        pageSize: 10,
        orderBy: 'asc',
        total: 0,
        title: "",
        username: "",
        state: "",
        // 所有文章标题
        titleArray:[]
    },
    beforeCreate(){
       vm = this;
    },
    mounted() {
        this.initCommentList();
        this.init();
    },
    methods: {
        init: function () {
            $.post("/post/allArticles",function (res) {
                if (res.code === 10000) {
                    vm.titleArray = res.data;
                }
            });
        },
        //评论列表
        initCommentList:function(){
            $.post("/comment/commentManageListPage", {title:this.title,pageSize: this.pageSize, pageNum: this.pageNum,orderBy:this.orderBy}, function (res) {
                if (res.code === 10000) {
                    vm.tableData = res.data.records;
                    vm.total = res.data.total;
                }
            });
        },
        formatterDate: function (row, column) {
            let date = new Date(row.createTime);
            let year = date.getFullYear();
            let month = change(date.getMonth());
            let day = change(date.getDate());
            var hour=change(date.getHours());
            var minute=change(date.getMinutes());

            function change(t) {
                if (t < 10) {
                    return "0" + t;
                } else {
                    return t;
                }
            }
            return year + "/" + month + "/" + day+" "+hour+":"+minute;
        },
        // 按文章标题查找
        queryTitleSearch: function(queryString, cb) {
            var titleArray = this.titleArray;
            var results = queryString ? titleArray.filter(this.createFilter(queryString)) : titleArray;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        createFilter(queryString) {
            return (titleArray) => {
                return (titleArray.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        // 选择文章标题
        selectTitle(item) {
            this.pageNum = 1;
            this.initCommentList();
        },
        titleIconClick(ev){
            this.pageNum = 1;
            this.initCommentList();
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
            this.initCommentList();
        },
        // 排序
        sortChange: function (column) {
            if(column.order === "ascending"){
                this.orderBy = "asc";
            }else{
                this.orderBy = "desc";
            }
            this.initCommentList();
        }

    }
});