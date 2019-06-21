'use strict';

let app = new Vue({
    el: "#app",
    data: {
        account: '',
        password: '',
        msgError: ''
    },
    methods: {
        login: function (event) {
            $.post('/u/login', {
                account: app.account,
                password: app.password
            },function (res) {
                if (res.code === 10000){
                    window.location.href = '/';
                }else{
                    app.msgError = res.msg;
                }
            })
        }
    }
});