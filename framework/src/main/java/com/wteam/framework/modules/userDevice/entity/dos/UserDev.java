package com.wteam.framework.modules.userDevice.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;


import java.util.Date;

/**
 * 员工设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@TableName("dp_user_dev")
public class UserDev extends BaseEntity {

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
     * 用户ID
     */
	private String userId;
    /**
     * 设备ID
     */
	private String devId;
}