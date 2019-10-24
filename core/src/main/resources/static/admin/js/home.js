$(function() {
    let vm;
    let app = new Vue({
        el: "#app",
        data: {},
        beforeCreate() {
            vm = this;
        },
        mounted() {
            this.getMonitorData();
        },
        methods: {
            getMonitorData() {
                let endTime = new Date();
                let startTime = new Date(endTime.valueOf());
                startTime.setDate(startTime.getDate()-1);
                $.post("/getMonitorData", {metric: "CPULoadAvg", period: 300, startTime:startTime,endTime:endTime},function (res) {
                    if (res.code === 10000){
                        let timestamps = res.data.dataPoints[0].timestamps;
                        let values = res.data.dataPoints[0].values;
                    }
                });
            }
        }
    });
});