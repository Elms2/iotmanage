package com.wteam.framework.modules.alarmRecord.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;

/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
@Data
@TableName("alarm_record")
public class AlarmRecord {

    /**
     * 告警源Id
     */
	private String sourceId;
    /**
     * 告警目标名称
     */
	private String targetName;
    /**
     * 处理时间
     */
	private String handleTime;
    /**
     * 告警目标Id
     */
	private String targetId;
    /**
     * 告警级别
     */
	private Integer level;
    /**
     * 最近一次告警时间
     */
	private String alarmTime;
    /**
     * 说明
     */
	private String description;
    /**
     * 告警配置名称
     */
	private String alarmName;
    /**
     * 告警目标类型
     */
	private String targetType;
    /**
     * 告警目标Key
     */
	private String targetKey;
    /**
     * 告警配置ID
     */
	private String alarmConfigId;
    /**
     * 告警源类型
     */
	private String sourceType;
    /**
     * id
     */
	private String id;
    /**
     * 告警源名称
     */
	private String sourceName;
    /**
     * 告警记录状态
     */
	private String state;
}