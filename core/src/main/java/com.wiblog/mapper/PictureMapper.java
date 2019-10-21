package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wiblog.entity.Picture;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-10-16
 */
public interface PictureMapper extends BaseMapper<Picture> {

    /**
     * 查找图片列表 时间排序 分页 返回日期
     * @param page page
     * @return IPage
     */
    IPage<Picture> selectPageList(Page<Picture> page);
}
