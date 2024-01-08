package com.wteam.framework.common.vo;
/**
 * @author Khai(951992121 @ qq.com)
 * @date 2022/9/6 3:19 PM
 */


import com.alibaba.fastjson.JSONObject;

import com.wteam.framework.common.utils.DateUtil;
import com.wteam.framework.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 日期搜索参数
 *
 * @author Chopper
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchVO implements Serializable {


    private String selecte;
    //@ApiModelProperty(value = "起始日期")
    private String startDate;

    //@ApiModelProperty(value = "结束日期")
    private String endDate;

    public Date getConvertStartDate() {
        if (StringUtils.isEmpty(startDate)) {
            return null;
        }
        return DateUtil.toDate(startDate, DateUtil.STANDARD_DATE_FORMAT);
    }

    public Date getConvertEndDate() {
        if (StringUtils.isEmpty(endDate)) {
            return null;
        }
        //结束时间等于结束日期+1天 -1秒，
        Date date = DateUtil.toDate(endDate, DateUtil.STANDARD_DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public Map<String, Object> getSelecte() {
        Map<String, Object> map = JSONObject.parseObject(selecte, Map.class);
        return map;
    }
}

