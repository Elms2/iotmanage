package com.wteam.framework.modules.entInfo.entity.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wteam.framework.modules.file.entity.dos.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.wteam.framework.common.mybatis.BaseEntity;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dp_ent_info")
public class EntInfo extends BaseEntity {

    /**
     * ID
     */
//	private String id;
    /**
     * 创建者
     */
//	private String createBy;
    /**
     * 创建时间
     */
//	private Date createTime;
    /**
     * 删除标志 true/false 删除/未删除
     */
//	private Boolean deleteFlag;
    /**
     * 更新者
     */
//	private String updateBy;
    /**
     * 更新时间
     */
//	private Date updateTime;
    /**
     * logo
     */
    @TableField("en_logo")
	private String enLogo;
    /**
     * 项目名称
     */
    @TableField("project_name")
	private String projectName;
    /**
     * logo
     */
    @TableField("en_name")
	private String enName;
    /**
     * 企业pc端的id
     */
    @TableField("en_id")
	private String enId;
    /**
     * 企业的素材列表
     */
    @TableField(exist = false)
    private List<File> fileList;
}