package com.wiblog.core.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/10
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /*
    @ExceptionHandler(value = WiblogException.class)
    public String tipException(Exception e) {
        log.error("find exception:e={}",e.getMessage());
        e.printStackTrace();
        return "comm/error_500";
    }


    @ExceptionHandler(value = Exception.class)
    public String exception(Exception e){
        log.error("find exception:e={}",e.getMessage());
        e.printStackTrace();
        return "comm/error_404";
    }*/
}
