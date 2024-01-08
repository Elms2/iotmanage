package com.wteam.framework.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;

import java.util.Date;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2022/11/9 10:14 PM
 */
public class MatchTimeTools {

    /**
     * 参数验证
     * 1、活动起始时间必须大于当前时间
     * 2、验证活动开始时间是否大于活动结束时间
     *
     * @param startTime 活动开始时间
     * @param endTime   活动结束时间
     */
    public static void checkMatchTime(Date startTime, Date endTime) {

        if (startTime == null) {
            throw new ServiceException(ResultCode.MATCH_TIME_ERROR);
        }

        DateTime now = DateUtil.date();
        if (endTime != null && now.after(endTime)) {
            throw new ServiceException(ResultCode.MATCH_END_TIME_ERROR);
        }

        if (startTime != null && now.before(startTime)) {
            throw new ServiceException(ResultCode.MATCH_START_TIME_ERROR);
        }
    }
}
