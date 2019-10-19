'use strict';

let vm;
let app = new Vue({
    el: "#app",
    data: {
        pageNum: 1,
        records: [],
        loading: false,
        noMore: false,
        id:-1
    },
    beforeCreate(){
        vm = this;
    },
    mounted(){

    },
    computed: {
        disabled () {
            console.log("loading", this.loading);
            console.log("noMore", this.noMore);
            var isExit = $("#"+vm.id).length>0;
            return this.loading || this.noMore;
        }
    },
    methods:{
        init(){

        },
        load(){
            this.loading = true;
            this.$http.get('/getImageList', {params: {"pageNum": this.pageNum,"pageSize": 10}}).then(function (response) {
                var records = response.data.data.records;
                vm.records = vm.records.concat(records);
                vm.id = vm.records[vm.records.length-1].id;
                vm.noMore = vm.records.length >= response.data.data.total;
                vm.loading = false;
                vm.pageNum++;
            })
        }
    }
});