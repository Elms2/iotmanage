package com.wteam.framework.modules.entInfo.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-12
 */
@Data
@ApiModel(value = "厂商、企业PC端")
public class EntInfoDTO implements Serializable {
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


	@ApiModelProperty(value = "logo")
	private String enLogo;


	@ApiModelProperty(value = "项目名称")
	private String projectName;


	@ApiModelProperty(value = "logo")
	private String enName;


	@ApiModelProperty(value = "企业pc端的id")
	private String enId;


}