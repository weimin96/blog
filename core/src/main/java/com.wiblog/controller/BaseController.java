package com.wiblog.controller;

import com.wiblog.utils.MapCache;

/**
 * 基础Controller
 *
 * @author pwm
 * @date 2019/7/3
 */
public abstract class BaseController {

    protected MapCache cache = MapCache.single();
}
