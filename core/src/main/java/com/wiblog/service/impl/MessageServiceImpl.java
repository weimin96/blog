package com.wiblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Message;
import com.wiblog.mapper.MessageMapper;
import com.wiblog.service.IMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author pwm
 * @since 2019-11-06
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Override
    public ServerResponse getMessageCount(Long id) {
        List<Map> list = messageMapper.selectCountList(id);
        return ServerResponse.success(list);
    }
}
