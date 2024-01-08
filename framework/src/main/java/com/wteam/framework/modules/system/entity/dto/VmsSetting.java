package com.wteam.framework.modules.system.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : VoiceSetting
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/5 12:06]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/5 12:06]
 * @updateRemark : [描述说明本次修改内容]
 */
@Data
public class VmsSetting implements Serializable {

    private String accessKeyId;

    private String accessSecret;

    private String signName;
}