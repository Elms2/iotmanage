package com.wteam.framework.modules.userRole.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wteam.framework.modules.userRole.entity.dos.UserRole;
import com.wteam.framework.modules.userRole.mapper.UserRoleMapper;
import com.wteam.framework.modules.userRole.service.UserRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public void updateUserRole(String userId, List<UserRole> userRoles) {

        //删除
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        this.remove(queryWrapper);

        //保存
        this.saveBatch(userRoles);
    }
}