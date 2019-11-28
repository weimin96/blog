package com.wiblog.core.scheduled;

import com.wiblog.core.common.Constant;
import com.wiblog.core.mapper.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 点赞定时器
 *
 * @author pwm
 * @date 2019/11/15
 */
@Slf4j
@Component
//@EnableScheduling
public class RecordScheduled {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Scheduled(cron = "* * 0/2 * * ?")
    public void recordHit(){
        Map<Object,Object> hitMap = redisTemplate.opsForHash().entries(Constant.HIT_RECORD_KEY);
        Iterator<Map.Entry<Object, Object>> it = hitMap.entrySet().iterator();
        List<Map> dataList = new ArrayList<>();
        while (it.hasNext()){
            Map.Entry<Object, Object> itData = it.next();
            Map<String,Object> data = new HashMap<>(2);
            data.put("id",itData.getKey());
            data.put("hits",itData.getValue());
            dataList.add(data);
            System.out.println("==");
        }
        articleMapper.updateHitsBatch(dataList);
    }

}
