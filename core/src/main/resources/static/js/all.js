(function () {

    'use strict';

    // 菜单栏
    var offcanvas = function () {
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
    });
}());

function dateFormat(d) {
    var date=new Date(d);
    var year=date.getFullYear();
    var month=switchNum(date.getMonth());
    var day=change(date.getDate());
    function change(t){
        if(t<10){
            return "0"+t;
        }else{
            return t;
        }
    }
    function switchNum(month) {
        var arry=["一","二","三","四","五","六","七","八","九","十","十一","十二"];
        return arry[Number(month)];
    }

    return month+"月"+day+", "+year;
}

function dateFormat2(d) {
    var date=new Date(d);
    var year=date.getFullYear();
    var month=change(date.getMonth());
    var day=change(date.getDate());
    var hour=change(date.getHours());
    var minute=change(date.getMinutes());
    function change(t){
        if(t<10){
            return "0"+t;
        }else{
            return t;
        }
    }

    return year+"."+month+"."+day+" "+hour+":"+minute;
}


