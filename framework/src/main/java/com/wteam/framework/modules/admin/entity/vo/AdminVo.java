package com.wteam.framework.modules.admin.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import lombok.Data;

import java.util.List;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/2/3 15:59
 */
@Data
public class AdminVo extends Admin {

    @TableField(exist = false)
    private List<Admin> enterprises;
}
