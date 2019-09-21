$(function(){
    //菜单点击
    /*J_iframe
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        $("#J_iframe").attr('src',url);
        return false;
    });*/

    $(".aside-select-li").on("click",function(){
        if($(this).hasClass("aside-select")){
            $(this).removeClass("aside-select");
        }else{
            $(this).addClass("aside-select");
        }
    })
    
    $(".menu-fold").on("click",function () {
        
    })
});

