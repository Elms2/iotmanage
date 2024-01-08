package com.wteam.framework.modules.member.entity.vo;

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
        tables = "dp_admin da, dp_member df,dp_ent_info di",          // 两表关联
        where = " df.en_id = da.id and df.en_id =di.en_id ",
        autoMapTo = "df"

)
public class MemberVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @DbField("di.en_name")
    private String enName;

    @ApiModelProperty(value = "ID")
    private Long id;


    @ApiModelProperty(value = "创建者")
    private String createBy;


    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    @ApiModelProperty(value = "删除标志 true/false 删除/未删除")
    private Boolean deleteFlag;


    @ApiModelProperty(value = "更新者")
    private String updateBy;


    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    @ApiModelProperty(value = "会员状态")
    private Boolean disabled;


    @ApiModelProperty(value = "会员头像")
    private String face;





    @ApiModelProperty(value = "手机号码")
    private String phone;


    @ApiModelProperty(value = "会员昵称")
    private String nickName;


    @ApiModelProperty(value = "会员密码")
    private String password;


    @ApiModelProperty(value = "会员性别")
    private Integer sex;


    @ApiModelProperty(value = "会员用户名")
    private String username;


    @ApiModelProperty(value = "企业Id")
    private Long enId;


    @DbField("da.parent_id")
    private Long parentId;


    private Integer role;




}