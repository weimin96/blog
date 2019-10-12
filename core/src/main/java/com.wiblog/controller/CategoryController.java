package com.wiblog.controller;


import com.wiblog.aop.AuthorizeCheck;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.Category;
import com.wiblog.service.ICategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *  控制层
 *
 * @author pwm
 * @date 2019-06-15
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    private ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/getCategory")
    public ServerResponse getCategory(){
        List<Category> list = categoryService.list();
        return ServerResponse.success(list,"获取分类列表成功");
    }

    /**
     * 添加分类
     * @param name name
     * @param parentId parentId
     * @return ServerResponse
     */
    @AuthorizeCheck(grade = "2")
    @PostMapping("/addCategory")
    public ServerResponse addCategory(String name,String url,Long parentId){
        Category category = new Category();
        category.setName(name);
        category.setUrl(url);
        Category parent = categoryService.getById(parentId);
        if (parent == null){
            return ServerResponse.error("不存在该父级分类",50001);
        }
        category.setParentId(parentId);
        boolean result = categoryService.save(category);
        if (!result){
            return ServerResponse.error("新增分类失败",50002);
        }
        return ServerResponse.success(null,"新增分类成功");
    }
}
