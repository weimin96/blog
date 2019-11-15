package com.wiblog.controller;

import com.wiblog.common.Constant;
import com.wiblog.common.ServerResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/11/15
 */
@RestController
public class LikesRecordController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 点赞
     *
     * @param articleId 文章id
     * @param commentId 评论id(可选)
     * @param type      类型 article comment
     * @return ServerResponse
     */
    @PostMapping("/record/like")
    public ServerResponse like(Long articleId, Long commentId, String type) {
        Integer count = null;
        if ("article".equals(type)) {
            count = (Integer) redisTemplate.opsForValue().get(Constant.LIKE_RECORD_KEY + type + articleId);
            if (count == null) {
                count = 0;
            }
            count++;
            redisTemplate.opsForValue().set(Constant.LIKE_RECORD_KEY + type + articleId, count);
        } else if ("comment".equals(type)) {
            count = (Integer) redisTemplate.opsForHash().get(Constant.LIKE_RECORD_KEY + type + articleId, commentId);
            if (count == null) {
                count = 0;
            }
            count++;
            redisTemplate.opsForHash().put(Constant.LIKE_RECORD_KEY + type + articleId, commentId, count);
        } else {
            return ServerResponse.error("类型错误", 30001);
        }
        return ServerResponse.success(count);

    }
}
