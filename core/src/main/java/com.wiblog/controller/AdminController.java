package com.wiblog.controller;

import com.wiblog.common.ServerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/8/2
 */
@RestController
public class AdminController {

    @Autowired
    private SystemInfo si;

    @GetMapping
    @RequestMapping("/systemStatus")
    public ServerResponse systemStatus(HttpServletRequest request, Map<String,Object> model){

        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();
        DecimalFormat df=new DecimalFormat("#.#");
        String totalMemory = df.format(memory.getTotal()/1024.0/1024/1024);
        String usedMemory = df.format(memory.getAvailable()/1024.0/1024/1024);
        String freeMemory = df.format(Double.valueOf(totalMemory)-Double.valueOf(usedMemory));
        double usedCpu = hal.getProcessor().getSystemCpuLoad() * 100;
        model.put("freeMemory", freeMemory);
        model.put("totalMemory", totalMemory);
        model.put("usedMemory", usedMemory);
        model.put("usedCpu", usedCpu);
        return ServerResponse.success(model,"");
    }

    @Bean
    public SystemInfo systemInfo(){
        return new SystemInfo();
    }
}
