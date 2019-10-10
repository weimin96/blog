package com.wiblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wiblog.common.ServerResponse;
import com.wiblog.entity.User;
import com.wiblog.entity.UserRole;
import com.wiblog.mapper.UserRoleMapper;
import com.wiblog.service.IUserRoleService;
import com.wiblog.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务实现类
 *
 * @author pwm
 * @since 2019-10-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public ServerResponse assignPermission(User user, Long uid, Long id) {
        // 分配权限
        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        userRole.setRoleId(id);
        int count = userRoleMapper.insert(userRole);
        if (count <=0){
            return ServerResponse.error("权限分配失败",90003);
        }
        return ServerResponse.success(null,"权限分配成功");
    }

    @Override
    public ServerResponse getUserRole(Long uid) {
        RoleVo role = userRoleMapper.selectRoleByUid(uid);
        return ServerResponse.success(role);
    }

    @Override
    public ServerResponse getRole() {
        List<RoleVo> list = userRoleMapper.selectRole();
        return ServerResponse.success(list);
    }
}
