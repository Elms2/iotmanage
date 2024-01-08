package com.wteam.framework.common.jetlinks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : MessageDto
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/7 20:57]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/7 20:57]
 * @updateRemark : [描述说明本次修改内容]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String dashboard;
    private String dimension;
    private String group;
    private String measurement;
    private String object;
    Map<String,String> params;
}
