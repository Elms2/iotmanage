package com.wteam.framework.modules.entDevice.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : Dashboard
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/7 11:41]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/7 11:41]
 * @updateRemark : [描述说明本次修改内容]
 */
@Data
@NoArgsConstructor
public class Dashboard {
    private String group;
    private DashboardData data;


    public Dashboard(String group) {
        this.group = group;
    }
}
