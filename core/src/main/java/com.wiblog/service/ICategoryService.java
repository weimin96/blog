package com.wiblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Category;

/**
 *   服务类
 *
 * @author pwm
 * @since 2019-06-15
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 删除分类
     * @param id id
     * @return ServerResponse
     */
    ServerResponse delCategory(Long id);
}
