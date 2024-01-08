package com.wteam.framework.modules.menu.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@ApiModel(value = "")
public class MenuDTO implements Serializable {
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


	@ApiModelProperty(value = "说明备注")
	private String description;


	@ApiModelProperty(value = "前端路由")
	private String frontRoute;


	@ApiModelProperty(value = "图标")
	private String icon;


	@ApiModelProperty(value = "层级")
	private Integer level;


	@ApiModelProperty(value = "菜单/权限名称")
	private String name;


	@ApiModelProperty(value = "父id")
	private String parentId;


	@ApiModelProperty(value = "赋权API地址,正则表达式")
	private String path;


	@ApiModelProperty(value = "排序值")
	private BigDecimal sortOrder;


	@ApiModelProperty(value = "菜单标题")
	private String title;


	@ApiModelProperty(value = "文件地址")
	private String frontComponent;


	@ApiModelProperty(value = "权限url")
	private String permission;


}