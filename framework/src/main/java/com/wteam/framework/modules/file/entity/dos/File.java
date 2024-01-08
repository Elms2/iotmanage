package com.wteam.framework.modules.file.entity.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wteam.framework.common.mybatis.BaseEntity;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 文件系统
 *
 * @author Chopper
 * @since 2020/11/26 15:35
 */
@Data
@TableName("dp_file")
@ApiModel(value = "文件")
public class File extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "原文件名")
    private String name;

    @ApiModelProperty(value = "存储文件名")
    private String fileKey;

    @ApiModelProperty(value = "大小")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "路径")
    private String url;

    @ApiModelProperty(value = "拥有者id")
    private String ownerId;

    @ApiModelProperty(value = "拥有者用户名")
    private String ownerName;


    private String enId;

    private String state;

    private String opened;

    private String status;

    @TableField(exist = false)
    private EntInfo entInfo;


    private String description;


}