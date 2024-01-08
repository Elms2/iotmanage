package com.wteam.framework.common.enums;
/**
 * @author Khai(951992121 @ qq.com)
 * @date 2022/9/4 3:29 PM
 */
/**
 * 安全相关常量
 *
  */
public enum SecurityEnum {

    /**
     * 存在与header中的token参数头 名
     */
    HEADER_TOKEN("accessToken"), USER_CONTEXT("userContext"), JWT_SECRET("secret"), UUID("uuid");

    String value;

    SecurityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}