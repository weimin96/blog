var app = new Vue({
    el: "#app",
    data:{

    },
    created() {
        this.initView();
    },
    methods: {
        articleEdit: function (id) {
            console.log("111");
            app.$router.push({
                path: '../articleEdit',
                // name: 'mallList',
                query: {
                    articleId: id
                }
            });
        },
        initView: function(){
            console.log("初始化");

        }
    }
});

function articleEdit1(id) {
    // app.articleEdit(id);

}

// $('#tableData').bootstrapTable({
//     url: '/post/articles',
//     dataField: "records",
//     dataType: "json",
//     method: "post",//请求方式
//     pagination: true,
//
//     pageList: [5, 10, 20, 50],//分页步进值
//     sidePagination: "server",//服务端分页
//     pageSize: 5, // 单页记录数
//
//     contentType : "application/x-www-form-urlencoded",
//     showRefresh: true,//刷新按钮
//     showColumns: true,//列选择按钮
//     buttonsAlign: "right",//按钮对齐方式
//     toolbar: "#toolbar",//指定工具栏
//     toolbarAlign: "left",//工具栏对齐方式
//     showToggle:true,
//     queryParams: function getParams(params) {
//         return {
//             pageSize: params.limit,
//             pageNum: params.offset/params.limit+1
//         };
//     },
//     responseHandler:function(res){
//         return res.data;
//     },
//     columns: [
//         {
//             title: "全选",
//             field: "select",
//             checkbox: true,
//             width: 20,//宽度
//             align: "center",//水平
//             valign: "middle"//垂直
//         }, {
//             field: 'title',
//             title: '标题'
//         }, {
//             field: 'articleCategories',
//             title: '分类'
//         }, {
//             field: 'tags',
//             title: '标签'
//         }, {
//             field: 'info',
//             formatter: 'infoFormat',
//             title: '统计'
//         },{
//             field: 'createTime',
//             title: '发布时间',
//             formatter: 'dateFormat',
//             sortable: true,// 是否可排序
//             order: "asc"// 默认排序方式
//         },{
//             field: 'operate',
//             title: '操作',
//             formatter: 'operateFormat'
//         }]
// });
//
// function infoFormat(value, row, index) {
//     return "评论" + row.commentsCounts + "-喜欢" + row.likes + "-点击" + row.hits;
// }
//
// function operateFormat(value, row, index) {//赋予的参数
//     return [
//         '<a class="" href="'+row.articleUrl+'">查看</a>&nbsp;',
//         '<a class="" href="./articleEdit?id='+row.id+'">编辑</a>&nbsp;',
//         '<a class="" href="#">删除</a>'
//     ].join('');
// }
//
// function dateFormat(value, row, index) {
//     let date = new Date(value);
//     let year = date.getFullYear();
//     let month = date.getMonth();
//     let day = change(date.getDate());
//
//     function change(t){
//         if(t<10){
//             return "0"+t;
//         }else{
//             return t;
//         }
//     }
//     return year+"/"+month+"/"+day;
// }