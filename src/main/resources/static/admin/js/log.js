
let vm;
let app = new Vue({
    el: "#app",
    data() {
        return{
            websocket:{}
        }
    },
    beforeCreate() {
        vm = this;
    },
    mounted() {
        this.createWebsocket();
    },
    methods: {
        createWebsocket(){
            // this.websocket = new WebSocket('wss://www.wiblog.cn/websocket/log');
            this.websocket = new WebSocket('wss://127.0.0.1/websocket/log');
            this.websocket.onopen = this.open;
            this.websocket.onmessage = this.onmessage();
        },
        // websocket连接
        open(){
            console.log("socket连接成功");
            let token = Vue.$cookies.get("uToken");
            this.websocket.send(token);
        },
        onmessage(){
            $("#log-container div").append(event.data);
        }
    }
});