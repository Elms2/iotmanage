package com.wteam.framework.modules.userRole.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@NoArgsConstructor
@TableName("dp_user_role")
public class UserRole implements Serializable {

    /**
     * ID
     */
	private String id;
    /**
     * 角色唯一id
     */
	private String roleId;
    /**
     * 用户唯一id
     */
	private String userId;


    public UserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }


}