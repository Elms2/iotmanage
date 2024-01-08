package com.wteam.framework.modules.deviceInstance.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ejlchina.searcher.bean.DbField;
import com.ejlchina.searcher.bean.DbType;
import com.ejlchina.searcher.bean.SearchBean;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Chopper
 * @since 2020/11/26 15:35
 */
@Data
@SearchBean(
        tables = "dp_admin da, dev_device_instance dv,dp_ent_dev de",
        where = " de.en_id = da.id  and de.dev_id = dv.id and da.platform_flag=1",
        autoMapTo = "dv"

)
public class DeviceVo implements Serializable {

    private static final long serialVersionUID = 1L;

//    private String entName;


    //企业id
    //设备id
    //厂商id
    @DbField("da.parent_id")
    private Long parentId;


    @DbField(value = "de.en_id")
    private Long enId;
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
    private Long registryTime;
    /**
     * 机构ID
     */
    private String orgId;
//    /**
//     * 父级设备ID
//     */
//    private String parentId;

    /**
     * 设备特性
     */
    private Long features;
    /**
     * 图片地址
     */
    private String photoUrl;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Long modifyTime;
    /**
     * 创建时间(只读)
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss”)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long createTime;


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


}