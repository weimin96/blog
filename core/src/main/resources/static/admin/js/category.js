var vm;
var app = new Vue({
    el: "#app",
    data: {
        categoryList: [],
    },
    beforeCreate(){
       vm = this;
    },
    mounted() {
        this.init();
    },
    methods: {
        init: function () {
            this.getCategory();
        },
        // 获取分类列表
        getCategory: function () {
            $.get("/category/getCategory", function (data) {
                if (data.code === 10000) {
                    vm.categoryList = vm.setCategoryTree(data.data,0,1);
                }
            });
        },
        // 构造分类级联列表
        setCategoryTree: function(data,pid,level){
            let tree = [];
            for (let i = 0; i < data.length; i++) {
                if(data[i].parentId === pid){
                    data[i].children=vm.setCategoryTree(data,data[i].id,level+1);
                    data[i].level = level;
                    tree.push(data[i]);
                }
            }
            if (tree.length === 0){
                return null;
            }
            return tree;
        },
    }
});