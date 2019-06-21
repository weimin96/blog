'use strict';

let app = new Vue({
    el: "#app",
    data: {
        username: "",
        phone: "",
        email: "",
        password: "",
        confirmPassword: "",
        msgName: "",
        msgPhone: "",
        msgEmail: "",
        msgConfirmPassword: "",
        msgPassword: "",
        nameTag: "0",
        phoneTag: "0",
        emailTag: "0",
        passwordTag: "0",
        confirmPasswordTag: "0"
    },
    methods: {
        checkName: function () {
            $.post('/u/checkUsername', {
                value: app.username
            }, function (res) {
                if (res.code === 10000) {
                    app.msgName = '';
                    app.nameTag = '1';
                    return true;
                } else {
                    app.msgName = res.msg;
                    app.nameTag = '2';
                    return false;
                }
            });
        },
        checkPhone: function () {
            if (this.phone.length === 0) {
                this.msgPhone = '';
                this.phoneTag = '0';
                return true;
            }
            $.post('/u/checkPhone', {
                value: app.phone
            }, function (res) {
                if (res.code === 10000) {
                    app.msgPhone = '';
                    app.phoneTag = '1';
                    return true;
                } else {
                    app.msgPhone = res.msg;
                    app.phoneTag = '2';
                    return false;
                }
            });
        },
        checkEmail: function () {
            if (this.email.length === 0) {
                this.msgEmail = '';
                this.emailTag = '0';
                return true;
            }
            $.post('/u/checkEmail', {
                value: app.email
            }, function (res) {
                if (res.code === 10000) {
                    app.msgEmail = '';
                    app.emailTag = '1';
                    return true;
                } else {
                    app.msgEmail = res.msg;
                    app.emailTag = '2';
                    return false;
                }
            });
        },
        checkPassword: function () {
            if (this.password.length < 6) {
                this.msgPassword = '密码长度必须大于6位';
                this.passwordTag = '2';
                return false;
            } else {
                this.msgPassword = '';
                this.passwordTag = '1';
                return true;
            }
        },
        checkConfirmPassword: function () {
            if (this.password !== this.confirmPassword) {
                this.msgConfirmPassword = '两次密码不一致';
                this.confirmPasswordTag = '2';
                return false;
            } else {
                this.msgConfirmPassword = '';
                this.confirmPasswordTag = '1';
                return true;
            }
        },
        register: function (event) {
            if (this.checkName && this.checkPhone && this.checkEmail && this.checkPassword && this.checkConfirmPassword) {
                let data = {
                    username: app.username,
                    password: app.password
                };
                if (app.phone !== '') {
                    data.phone=app.phone;
                }
                if (app.email !== '') {
                    data.email=app.email;
                }
                $.post('/u/register', data, function (res) {
                    if (res.code === 10000) {
                        window.location.href = '/login';
                    } else {
                        alert(res.msg);
                    }
                })
            } else {
                event.preventDefault();
                console.log("错误");
                return false;
            }
        }
    }
});