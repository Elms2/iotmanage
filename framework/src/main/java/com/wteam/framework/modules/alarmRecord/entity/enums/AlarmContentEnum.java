package com.wteam.framework.modules.alarmRecord.entity.enums;

/**
 * @author : [LiuYanQiang]
 * @version : [v1.0]
 * @className : AlarmContent
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/5 14:41]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/5 14:41]
 * @updateRemark : [描述说明本次修改内容]
 */
public enum AlarmContentEnum {
    ALARM_CONTENT_79("测试");

    private final String value;

    AlarmContentEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
