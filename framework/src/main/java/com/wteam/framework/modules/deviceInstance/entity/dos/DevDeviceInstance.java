package com.wteam.framework.modules.deviceInstance.entity.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

/**
 * 设备信息表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-25
 */
@Data
@TableName("dev_device_instance")
public class DevDeviceInstance {

    private String entName;


    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 配置信息
     */
    private String configuration;
    /**
     * 创建者ID(只读)
     */
    private String creatorId;
    /**
     * 创建者名称(只读)
     */
    private String creatorName;
    /**
     * 修改人ID
     */
    private String modifierId;
    /**
     * 派生(独立)物模型
     */
    private String deriveMetadata;
    /**
     * 激活时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String registryTime;
    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 父级设备ID
     */
    private String parentId;

    /**
     * 设备特性
     */
    private String features;
    /**
     * 图片地址
     */
    private String photoUrl;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private String modifyTime;
    /**
     * 创建时间(只读)
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss”)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;


    /**
     * 说明
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 设备ID(只能由数字,字母,下划线和中划线组成)
     */
    private String id;
    /**
     * 修改人名称
     */
    private String modifierName;
    /**
     * 状态(只读)
     */
    private String state;

    private String firmwareVersion;

    private String location;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 设备名称
     */
    private String name;


    private Integer sort;

}