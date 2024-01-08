package com.wteam.framework.modules.member.entity.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;


import java.util.Date;

/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@TableName("dp_member")
public class Member extends BaseEntity {

    /**
     * 会员状态
     */
	private Boolean disabled;
    /**
     * 会员头像
     */
	private String face;
    /**
     * 是否开通店铺
     */

    /**
     * 手机号码
     */
	private String phone;
    /**
     * 会员昵称
     */
	private String nickName;
    /**
     * 会员密码
     */
	private String password;
    /**
     * 会员性别
     */
	private Integer sex;
    /**
     * 会员用户名
     */
	private String username;
    /**
     * 企业Id
     */
	private String enId;
    /**
     * 用户的角色id
     */
    @TableField(exist = false)
	private String roleId;
    /**
     * 通讯地址
     */
    private String address;
    /**
     * 座机号码
     */
    private String tel;
    /**
     *邮箱
     */
    private String email;


    @TableField(exist = false)
    private Integer cnt;


    @TableField(exist = false)
    private EntInfo entInfo;

    private String role;


}