package com.wiblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wiblog.entity.UserRole;
import com.wiblog.vo.RoleVo;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author pwm
 * @since 2019-10-09
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<RoleVo> selectRoleByUid(Long uid);
}
