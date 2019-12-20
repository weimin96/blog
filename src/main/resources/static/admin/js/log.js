$(document).ready(function() {
    // 指定websocket路径
    var websocket = new WebSocket('ws://localhost:443/websocket/log');
    websocket.onmessage = function(event) {
        // 接收服务端的实时日志并添加到HTML页面中
        $("#log-container div").append(event.data);
        // 滚动条滚动到最低部
        $("#log-container").scrollTop($("#log-container div").height() - $("#log-container").height());
    };
});