const popularArticle = {
    template:'<div class="card">\n' +
        '                            <div class="sibar-title">\n' +
        '                                <span>发现</span><span>热门文章</span>\n' +
        '                            </div>\n' +
        '                            <div class="rank-list">\n' +
        '                                <ul>\n' +
        '                                    <li v-for="(item,index) in rankList" :key="index"><a>{{item.title}}</a></li>\n' +
        '                                </ul>\n' +
        '                            </div>\n' +
        '                        </div>',
    data(){
        return {
            rankList: []
        }
    },
    methods: {
        getArticleRank(_this) {
            $.get("/post/getArticleRank", function (res) {
                if (res.code === 10000) {
                    _this.rankList = res.data;
                }
            })
        },
    },
    created(){
        this.$bus.on('popular-article-init', () => {
            this.getArticleRank(this)
        })
    }
};