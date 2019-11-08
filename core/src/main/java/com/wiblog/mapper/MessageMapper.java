package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.Message;

import java.util.List;
import java.util.Map;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-11-06
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<Map> selectCountList(Long uid);
}
