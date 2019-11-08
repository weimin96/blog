package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.User;
import com.wiblog.entity.UserAuth;

import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-11-07
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {


    /**
     * 唯一性校验
     * @param identityType 类型
     * @param identifier 凭证
     * @return int
     */
    int checkUnique(@Param(value = "identityType")String identityType, @Param(value = "identifier")String identifier);


    /**
     * 登录
     * @param userAuth userAuth
     * @return User
     */
    User login(UserAuth userAuth);
}
