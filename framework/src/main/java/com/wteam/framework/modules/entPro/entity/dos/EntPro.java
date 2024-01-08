package com.wteam.framework.modules.entPro.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;


import java.util.Date;

/**
 * 企业产品中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-18
 */
@Data
@TableName("dp_ent_pro")
public class EntPro extends BaseEntity {


    /**
     * 企业ID
     */
	private String enId;
    /**
     * 产品ID
     */
	private String proId;
}