package com.wteam.framework.modules.admin.entity.enums;

/**
 *
 */
public enum userEnum {

    SUPER_ADMIN("超级管理员","1"),

    MANUFACTURER_ADMIN("厂商管理员","2"),

    ENTERPRISE_ADMIN("企业管理员","3"),

    ENTERPRISE_OPERATOR("企业操作员","4")
    ;


    private String description;

    private String roleCode;

    public String description() {
        return description;
    }

    public String roleCode() {return roleCode;}

    userEnum(String description,String roleCode) {
        this.description = description;
        this.roleCode=roleCode;
    }
}
