package com.wteam.framework.modules.role.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@ApiModel(value = "")
public class RoleDTO implements Serializable {
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


	@ApiModelProperty(value = "是否为注册默认角色")
	private Boolean defaultRole;


	@ApiModelProperty(value = "备注")
	private String description;


	@ApiModelProperty(value = "角色名")
	private String name;


}