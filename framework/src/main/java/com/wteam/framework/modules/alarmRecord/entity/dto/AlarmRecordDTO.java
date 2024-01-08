package com.wteam.framework.modules.alarmRecord.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
@ApiModel(value = "告警记录")
public class AlarmRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "告警源Id")
	private String sourceId;


	@ApiModelProperty(value = "告警目标名称")
	private String targetName;


	@ApiModelProperty(value = "处理时间")
	private String handleTime;


	@ApiModelProperty(value = "告警目标Id")
	private String targetId;


	@ApiModelProperty(value = "告警级别")
	private Integer level;


	@ApiModelProperty(value = "最近一次告警时间")
	private String alarmTime;


	@ApiModelProperty(value = "说明")
	private String description;


	@ApiModelProperty(value = "告警配置名称")
	private String alarmName;


	@ApiModelProperty(value = "告警目标类型")
	private String targetType;


	@ApiModelProperty(value = "告警目标Key")
	private String targetKey;


	@ApiModelProperty(value = "告警配置ID")
	private String alarmConfigId;


	@ApiModelProperty(value = "告警源类型")
	private String sourceType;


	@ApiModelProperty(value = "id")
	private String id;


	@ApiModelProperty(value = "告警源名称")
	private String sourceName;


	@ApiModelProperty(value = "告警记录状态")
	private String state;


}