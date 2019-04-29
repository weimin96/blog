(function () {

    'use strict';

    //文章列表
    var articleList = function (currentPage) {
        $.ajax({
            type: 'POST',
            url: '/articles',
            dataType: 'json',
            data: {
                pageNum: currentPage,
                pageSize: "1"
            },
            success: function (data) {
                var $article = $('#article');
                $article.empty();
                $.each(data.data.list,function (index,obj) {
                    var createTime=dateFormat(obj['createTime']);
                    var articleUrl=obj['articleUrl'];
                    var commentsCounts=obj['commentsCounts'];
                    var hits=obj['hits'];
                    var likes=obj['likes'];
                    var articleCategories=obj['articleCategories'];
                    var articleSummary=obj['articleSummary'];
                    var $center= $('<article class="post-card clearfix">'+
                        '<div class="clearfix">'+
                        '<div class="post-list-pic">'+
                        '<a href="'+articleUrl+'">'+
                        '<img src="'+obj['url']+'">'+
                        '</a>'+
                        '</div>'+
                        '<div class="post-list-inner">'+
                        '<header class="post-list-inner-header">'+
                        '<a class="label" href="https://ylws.me/category/tech">'+articleCategories+
                        '<i class="label-arrow"></i></a>'+
                        '<h2 class="post-list-inner-title">'+
                        '<a href="'+articleUrl+'">'+obj['title']+'</a></h2>'+
                        '</header>'+
                        '<div class="post-list-inner-content">'+
                        '<p>'+articleSummary+'</p>'+
                        '</div>'+
                        '</div>'+
                        '<div class="post-list-meta">'+
                        '<span class="visible-lg visible-md visible-sm pull-left">'+
                        '<a href="'+articleUrl+'">'+
                        '<i class="fa fa-calendar"></i>'+createTime+'</a>'+
                        '<a href="'+articleUrl+'#respond">'+
                        '<i class="fa fa-commenting-o"></i>'+commentsCounts+' 条评论</a>'+
                        '</span>'+
                        '<span class="pull-left">'+
                        '<a href="'+articleUrl+'">'+
                        '<i class="fa fa-eye"></i>'+hits+' 次阅读</a>'+
                        '<a href="'+articleUrl+'">'+
                        '<i class="fa fa-thumbs-o-up"></i>'+ likes+' 人点赞</a>'+
                        '</span>'+
                        '<span class="pull-right">'+
                        '<a class="read-more" href="'+articleUrl+'" title="阅读全文">阅读全文+'+
                        '<i class="fa fa-chevron-circle-right"></i>'+
                        '</a>'+
                        '</span>'+
                        '</div>'+
                        '</div>'+
                        '</article>');
                    $article.append($center);
                });
                //分页
                $("#pagination").paging({
                    pageNum:data.data['pageNum'],//当前所在页码
                    pages:data.data['pages'],//总页数
                    hasNextPage:data.data['hasNextPage'],//是否有下一页
                    isLastPage:data.data['isLastPage'],//是否最后一页
                    callback:function(currentPage){
                        articleList(currentPage);
                    }
                });
            }
        })
    };

    $(function () {
        articleList(1);
    });
}());