package com.wteam.framework.modules.alarmRecord.entity.enums;

/**
 * 系统设置常量
 *
 * @author Chopper
 * @since 2020/9/11 17:03
 */
public enum AlarmTypeEnum{
    ALARM_TYPE_01("测试"),
    ALARM_TYPE_02("故障");

    private final String value;

    AlarmTypeEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
