package com.wteam.framework.modules.admin.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@ApiModel(value = "厂商、企业PC端")
public class AdminDTO implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "ID")
	private String id;


	@ApiModelProperty(value = "创建者")
	private String createBy;


	@ApiModelProperty(value = "创建时间")
	private Date createTime;


	@ApiModelProperty(value = "删除标志 true/false 删除/未删除")
	private Boolean deleteFlag;


	@ApiModelProperty(value = "更新者")
	private String updateBy;


	@ApiModelProperty(value = "更新时间")
	private Date updateTime;


	@ApiModelProperty(value = "用户头像")
	private String avatar;


	@ApiModelProperty(value = "邮件")
	private String email;


	@ApiModelProperty(value = "是否是超级管理员 超级管理员/普通管理员")
	private Boolean isSuper;


	@ApiModelProperty(value = "昵称")
	private String nickName;


	@ApiModelProperty(value = "密码")
	private String password;


	@ApiModelProperty(value = "状态 默认true正常 false禁用")
	private Boolean status;


	@ApiModelProperty(value = "用户名")
	private String username;


	@ApiModelProperty(value = "0厂商，1企业")
	private Integer platformFlag;

	@ApiModelProperty(value = "上级id")
	private String parentId;


	@TableField(exist = false)
	private String role;

	@TableField(exist = false)
	private String factId;



}