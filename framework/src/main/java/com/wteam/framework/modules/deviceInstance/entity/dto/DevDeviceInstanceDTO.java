package com.wteam.framework.modules.deviceInstance.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 设备信息表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-25
 */
@Data
@ApiModel(value = "设备信息表")
public class DevDeviceInstanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "设备类型")
	private String deviceType;


	@ApiModelProperty(value = "产品ID")
	private String productId;


	@ApiModelProperty(value = "配置信息")
	private String configuration;


	@ApiModelProperty(value = "创建者ID(只读)")
	private String creatorId;


	@ApiModelProperty(value = "创建者名称(只读)")
	private String creatorName;


	@ApiModelProperty(value = "修改人ID")
	private String modifierId;


	@ApiModelProperty(value = "派生(独立)物模型")
	private String deriveMetadata;


	@ApiModelProperty(value = "激活时间")
	private String registryTime;


	@ApiModelProperty(value = "机构ID")
	private String orgId;


//	@ApiModelProperty(value = "父级设备ID")
//	private String parentId;


	@ApiModelProperty(value = "产品名称")
	private String productName;


	@ApiModelProperty(value = "设备特性")
	private String features;


	@ApiModelProperty(value = "图片地址")
	private String photoUrl;


	@ApiModelProperty(value = "修改时间")
	private String modifyTime;


	@ApiModelProperty(value = "创建时间(只读)")
	private String createTime;


	@ApiModelProperty(value = "设备名称")
	private String name;


	@ApiModelProperty(value = "说明")
	private String describe;


	@ApiModelProperty(value = "设备ID(只能由数字,字母,下划线和中划线组成)")
	private String id;


	@ApiModelProperty(value = "修改人名称")
	private String modifierName;


	@ApiModelProperty(value = "状态(只读)")
	private String state;

	private String enId;

	private String parentId;



}