$(function() {

    var setStatus = function() {
        $.get("/systemStatus",function (res) {

            if (res.code === 10000){
                console.log(res.data);
                var freeMemory = res.data.freeMemory;
                var totalMemory = res.data.totalMemory;
                var usedMemory = res.data.usedMemory;
                var usedCpu = res.data.usedCpu;
                $("#freeMemory").html(freeMemory);
                $("#totalMemory").html(totalMemory);
                var cpuData = [{
                    label: "剩余CPU",
                    data: 100-usedCpu,
                    color: "#37d346",
                }, {
                    label: "已使用",
                    data: usedCpu,
                    color: "#de4d40",
                }];
                var plotObjCpu = $.plot($("#flot-pie-chart-cpu"), cpuData, {
                    series: {
                        pie: {
                            show: true
                        }
                    },
                    legend: {
                        show: false
                    },
                    grid: {
                        hoverable: true
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
                        shifts: {
                            x: 20,
                            y: 0
                        },
                        defaultTheme: false
                    }
                });

                var data = [{
                    label: "剩余内存",
                    data: freeMemory,
                    color: "#37d346",
                }, {
                    label: "使用内存",
                    data: usedMemory,
                    color: "#de4d40",
                }];

                var plotObj = $.plot($("#flot-pie-chart"), data, {
                    series: {
                        pie: {
                            show: true
                        }
                    },
                    legend: {
                        show: false
                    },
                    grid: {
                        hoverable: true
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
                        shifts: {
                            x: 20,
                            y: 0
                        },
                        defaultTheme: false
                    }
                });
            }
        });
    };

    setStatus();
    setInterval(setStatus,20000);


});