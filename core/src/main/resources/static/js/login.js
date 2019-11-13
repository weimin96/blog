'use strict';

let app = new Vue({
    el: "#app",
    data: {
        account: '',
        password: '',
        msgError: ''
    },
    created(){
        this.init();
    },
    methods: {
        init: function () {
            if (user !== null){
                window.location.href = '/';
            }
        },
        login: function (event) {
            $.post('/u/login', {
                account: app.account,
                password: app.password
            },function (res) {
                if (res.code === 10000){
                    location.href = Cookies.get('back');
                }else{
                    app.msgError = res.msg;
                }
            })
        },
        githubLogin(){
            window.location.href = "https://github.com/login/oauth/authorize?client_id=9d543dc4501558c6759f&redirect_uri=https://127.0.0.1/u/github/callback&response_type=code&state=login";
        }
    }
});
