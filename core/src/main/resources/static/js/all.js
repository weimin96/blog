'use strict';


// 菜单栏
/*var offcanvas = function () {
    // 复制下拉菜单
    var $clone = $('#header-menu-wrap').clone();
    $clone.attr({
        'id': 'offcanvas-menu'
    });
    $clone.find('> ul').attr({
        'class': '',
        'id': ''
    });
    $('#index-page').prepend($clone);
    var $body = $('body');
    $('.js-nav-toggle').on('click', function () {
        // 隐藏滚动条

        if ($body.hasClass('offcanvas')) {
            $body.removeClass('offcanvas');
        } else {
            $body.addClass('offcanvas');
        }
    });
    $('#offcanvas-menu').css('height', $(window).height());
    $(window).resize(function () {
        var w = $(window);
        $('#offcanvas-menu').css('height', w.height());
        if (w.width() > 769) {
            if ($body.hasClass('offcanvas')) {
                $body.removeClass('offcanvas');
            }
        }
    });
};


$(function () {
    offcanvas();
});*/


/*function dateFormat(d) {
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
}*/

let vue = new Vue({
    el: "#index-header",
    data: {
        category: [],
        showSearch: false,
        isLogin: false,
        avatarImg:"",
        username: "",
        showUserMessage: false,
        showMessage: false,
        userUrl:"",
        messageCount:0,
        typeList:[0,0,0,0,0],
    },
    mounted() {
        this.getCategory();
        if (user != null){
            this.isLogin = true;
            this.avatarImg=user.avatarImg;
            this.username = user.username;
            this.userUrl = (+new Date(user.createTime))*3;
            this.getMessage();
        }
    },
    methods: {
        getMessage(){
            $.get("/getMessageCount", function (data) {
                if (data.code === 10000) {
                    $.each(data.data,function (index,item) {
                        vue.typeList[item.type]=item.count;
                        vue.messageCount +=item.count;
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
        showSearchBtn(e){
            this.showSearch = true;
            this.$nextTick(function (e) {
                $("#searchInput").focus();
            });

        },
        // 个人中心
        gotoUserCenter(){
            window.parent.location.href = "/user/"+this.userUrl;
        },
        logout(){
            $.get("/u/logout",function (res) {
                if (res.code === 10000){
                    window.location.reload();
                }
            })
        }
    }
});

Vue.component('menu-tree', {
    props: ['value'],
    template: '<li v-if="value.children"><a href="#">{{value.name}}</a>'+
        '<ul><menu-tree v-for="item in value.children" v-bind:value="item"></menu-tree></ul></li>'+
        '<li v-else><a class="menu-item" :href="\'category/\'+value.url">{{value.name}}</a></li>'
});


