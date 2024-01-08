package com.wteam.framework.modules.entDevice.entity.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : Data
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/7 11:47]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/7 11:47]
 * @updateRemark : [描述说明本次修改内容]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardData {
    private Long value;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeString;
    private String timestamp;
}
