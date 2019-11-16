'use strict';

let vue = new Vue({
    el: "#index-header",
    data: {
        category: [],
        focusSearchLabel: false,
        searchInput: "",
        isLogin: false,
        avatarImg: "",
        username: "",
        showUserMessage: false,
        showMessage: false,
        userUrl: "",
        messageCount: 0,
        typeList: [0, 0, 0, 0, 0],
    },
    mounted() {
        this.getCategory();
        if (user != null) {
            this.isLogin = true;
            this.avatarImg = user.avatarImg;
            this.username = user.username;
            this.userUrl = (+new Date(user.createTime)) * 3;
            this.getMessage();
        }
    },
    methods: {
        getMessage() {
            $.get("/getMessageCount", function (data) {
                if (data.code === 10000) {
                    $.each(data.data, function (index, item) {
                        vue.typeList[item.type] = item.count;
                        vue.messageCount += item.count;
                    });
                }
            });
        },
        getCategory: function () {
            $.get("/category/getCategory", function (data) {
                if (data.code === 10000) {
                    vue.category = vue.setCategoryTree(data.data, 0);
                    vue.$nextTick(function () {
                        $("#super-menu").superfish();
                    })
                }
            });
        },
        // 构造分类级联列表
        setCategoryTree: function (data, pid) {
            let tree = [];
            for (let i = 0; i < data.length; i++) {
                if (data[i].parentId === pid) {
                    data[i].value = data[i].id;
                    data[i].label = data[i].name;
                    data[i].children = this.setCategoryTree(data, data[i].id);
                    tree.push(data[i]);
                }
            }
            if (tree.length === 0) {
                return null;
            }
            return tree;
        },
        /*showSearchBtn(e){
            this.showSearch = true;
            this.$nextTick(function (e) {
                $("#searchInput").focus();
            });

        },*/
        focusSearch() {
            this.focusSearchLabel = true;
            $("#searchInput").focus();
        },
        blurSearch() {
            this.focusSearchLabel = false;
            this.searchInput = "";
        },
        // 个人中心
        gotoUserCenter() {
            window.parent.location.href = "/user/" + this.userUrl;
        },
        logout() {
            $.get("/u/logout", function (res) {
                if (res.code === 10000) {
                    window.location.reload();
                }
            })
        },
        login() {
            Cookies.set('back', window.location.href, {expires: 1, path: '/'});
            window.location.href = "/login";
        }
    }
});

Vue.component('menu-tree', {
    props: ['value'],
    template: '<li v-if="value.children"><a href="javascript:;">{{value.name}}</a>' +
        '<ul><menu-tree v-for="item in value.children" v-bind:value="item"></menu-tree></ul></li>' +
        '<li v-else><a class="menu-item" :href="\'category/\'+value.url">{{value.name}}</a></li>'
});


