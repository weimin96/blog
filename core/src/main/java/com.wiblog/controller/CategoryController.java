package com.wiblog.controller;


import com.wiblog.common.ServerResponse;
import com.wiblog.service.ICategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        return ServerResponse.success(categoryService.list(),"获取分类列表成功");
    }
}
