package com.wiblog.core.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 点赞定时器
 *
 * @author pwm
 * @date 2019/11/15
 */
@Slf4j
@Component
//@EnableScheduling
public class RecordLikeScheduled {

    @Scheduled(cron = "* * 0/2 * * ?")
    public void recordLike(){

    }
}
