package com.wteam.framework.modules.system.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;

import com.wteam.framework.common.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 设置
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("dp_setting")
@ApiModel(value = "配置")
@NoArgsConstructor
public class Setting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置值value")
    private String settingValue;

}