package com.wteam.framework.modules.userRole.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.modules.userRole.entity.dos.UserRole;

import java.util.List;

/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
public interface UserRoleService extends IService<UserRole> {

    void updateUserRole(String userId, List<UserRole> userRoles);
}