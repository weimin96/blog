package com.wiblog.controller;

import com.tencentcloudapi.monitor.v20180724.models.GetMonitorDataResponse;
import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.common.ServerResponse;
import com.wiblog.thirdparty.MonitorData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/8/2
 */
@RestController
public class AdminController {

    @Autowired
    private MonitorData monitorData;

    @AuthorizeCheck(grade = "2")
    @PostMapping("/getMonitorData")
    public ServerResponse getMonitorData(String metric, Integer period, Date startTime, Date endTime) {
        GetMonitorDataResponse res = monitorData.getMonitorData(metric,period,startTime,endTime);
        if (res == null || res.getDataPoints()==null){
            return ServerResponse.error("获取监控数据失败",30001);
        }
        return ServerResponse.success(res);
    }
}
