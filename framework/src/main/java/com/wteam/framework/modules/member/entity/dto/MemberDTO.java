package com.wteam.framework.modules.member.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Data
@ApiModel(value = "用户表")
public class MemberDTO implements Serializable {
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


	@ApiModelProperty(value = "会员状态")
	private Boolean disabled;


	@ApiModelProperty(value = "会员头像")
	private String face;




	@ApiModelProperty(value = "手机号码")
	private String phone;


	@ApiModelProperty(value = "会员昵称")
	private String nickName;


	@ApiModelProperty(value = "会员密码")
	private String password;


	@ApiModelProperty(value = "会员性别")
	private Integer sex;


	@ApiModelProperty(value = "会员用户名")
	private String username;


	@ApiModelProperty(value = "企业Id")
	private String enId;

	private String parentId;

	private String role;


}