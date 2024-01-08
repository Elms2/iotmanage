package com.wteam.framework.modules.role.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;


import java.util.Date;

/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@TableName("dp_role")
public class Role extends BaseEntity {

    /**
     * ID
     */
	private String id;
    /**
     * 创建者
     */
	private String createBy;
    /**
     * 创建时间
     */
	private Date createTime;
    /**
     * 删除标志 true/false 删除/未删除
     */
	private Boolean deleteFlag;
    /**
     * 更新者
     */
	private String updateBy;
    /**
     * 更新时间
     */
	private Date updateTime;
    /**
     * 是否为注册默认角色
     */
	private Boolean defaultRole;
    /**
     * 备注
     */
	private String description;
    /**
     * 角色名
     */
	private String name;
}