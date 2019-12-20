$(document).ready(function() {
    // 指定websocket路径
    var websocket = new WebSocket('wss://www.wiblog.cn/websocket/log');
    websocket.onmessage = function(event) {
        // 接收服务端的实时日志并添加到HTML页面中
        $("#log-container div").append(event.data);
        // 滚动条滚动到最低部
        //$("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
    };

    websocket.onopen = function(event) {
        console.log("连接")
    };

    websocket.onclose = function(event) {
        console.log('websocket 断开: ' + event.code + ' ' + event.reason + ' ' + event.wasClean)
        console.log("关闭");
    };

    websocket.onerror = function(event) {
        console.log("错误");
    }
});