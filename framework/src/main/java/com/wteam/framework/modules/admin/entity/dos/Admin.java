package com.wteam.framework.modules.admin.entity.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@TableName("dp_admin")
public class Admin extends BaseEntity {

    /**
     * 用户头像
     */
	private String avatar;
    /**
     * 邮件
     */
	private String email;
    /**
     * 是否是超级管理员 超级管理员/普通管理员
     */
	private Boolean isSuper;
    /**
     * 昵称
     */
	private String nickName;
    /**
     * 密码
     */
	private String password;
    /**
     * 状态 默认true正常 false禁用
     */
	private Boolean status;
    /**
     * 用户名
     */
	private String username;
    /**
     * 0厂商，1企业
     */
	private Integer platformFlag;

    /**
     * 上级id
     */
    private String parentId;

    private String phone;

    @TableField(exist = false)
    private String userRole;

    @TableField(exist = false)
    private String logo;

    @TableField(exist = false)
    private EntInfo entInfo;


    @TableField(exist = false)
    private String enterpriseName;


    @TableField(exist = false)
    private String projectName;


    /**
     * 通讯地址
     */
    private String address;
    /**
     * 座机号码
     */
    private String tel;








}