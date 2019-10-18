package com.wiblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Category;
import com.wiblog.mapper.ArticleMapper;
import com.wiblog.mapper.CategoryMapper;
import com.wiblog.service.ICategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-06-15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse delCategory(Long id) {
        // 改变文章的分类
        articleMapper.updateByCategoryId(id);
        // 将该分类的子节点移到上一层
        categoryMapper.updateSubCategory(id);
        categoryMapper.deleteById(id);
        return ServerResponse.success(null,"删除分类成功");
    }
}
