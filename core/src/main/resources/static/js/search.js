let vm;
let app = new Vue({
    el: "#app",
    data: {},
    beforeCreate() {
        vm = this;
    },
    mounted() {
        this.getSearchData();
    },
    methods: {
        getSearchData(){
            $.get("/post/searchArticle",{keyword:"系统"},function (res) {
                if (res.code===10000){

                }
            })
        }
    }
});