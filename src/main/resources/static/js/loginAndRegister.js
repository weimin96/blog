'use strict';

$(function () {
    layui.use(['form', 'layer'], function () {
        console.log("register");
        var form = layui.form;
        form.on('submit(login)', function (data) {
            console.log(data.field);
        });

        form.on('submit(register)', function (data) {
            console.log(data.field);

            var phone = data.field.phone;
            if (!(/^((13[0-9])|(15[^4,\D])|(17[0-9])|(18[0,5-9]))\d{8}$/.test(phone))) {
                alert("手机号码有误，请重填");
                return false;
            }
            /*
            $('#user').blur(function() {
                var user = $(this).val();
                $.ajax({
                    url: '/u/register',
                    type: 'post',
                    dataType: 'json',
                    data: {user: user},
                    //验证用户名是否可用
                    success: function (data) {
                        if (data == 1) {
                            $('#ri').removeAttr('hidden');
                            $('#wr').attr('hidden', 'hidden');


                        } else {
                            $('#wr').removeAttr('hidden');
                            $('#ri').attr('hidden', 'hidden');
                            layer.msg('当前用户名已被占用! ')

                        }

                    }
                });
            });*/
            return false;
        });

        form.verify({
            checkValid: function (value, item) {

            }
            , username: function (value, item) { //value：表单的值、item：表单的DOM对象
                var parent = 'us';
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    showWN(parent);
                    return '用户名不能有特殊字符';
                }
                if (/(^_)|(__)|(_+$)/.test(value)) {
                    showWN(parent);
                    return '用户名首尾不能出现下划线\'_\'';
                }
                showRN(parent);
            }

            //我们既支持上述函数式的方式，也支持下述数组的形式
            //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
            , pass: [
                /^[\S]{6,12}$/
                , '密码必须6到12位，且不能出现空格'
            ]
        });
    });
});

function checkValid(type) {
    let $input, uri, parent;
    if ('username' === type) {
        $input = $('#username');
        uri = '/u/checkUsername';
        parent = 'us';
    } else if ('phone' === type) {
        $input = $('#phone');
        if ($input.val() === "") {
            return;
        }
        uri = '/u/checkPhone';
        parent = 'ph';
    } else {
        $input = $('#email');
        if ($input.val() === "") {
            return;
        }
        uri = '/u/checkEmail';
        parent = 'em';
    }

    $.ajax({
        uri: uri,
        type: 'post',
        dataType: 'json',
        data: {
            value: $input.val()
        },
        success: function (data) {
            if (data.code === 10000) {
                showRN(parent);
            } else {
                showWN(parent);
            }
        }
    })
}

// 显示对号
function showRN(parent) {
    $('#' + parent + ' .rn').removeAttr('hidden');
    $('#' + parent + ' .wn').attr('hidden', 'hidden');
}

// 显示错号
function showWN(parent) {
    $('#' + parent + ' .rn').attr('hidden', 'hidden');
    $('#' + parent + ' .wn').removeAttr('hidden');
}