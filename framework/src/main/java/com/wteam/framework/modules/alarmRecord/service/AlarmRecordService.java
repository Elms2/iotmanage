package com.wteam.framework.modules.alarmRecord.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.alarmRecord.entity.dos.AlarmRecord;

import java.util.Map;

/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
public interface AlarmRecordService extends IService<AlarmRecord> {

    IPage<AlarmRecord> rePage(Page<AlarmRecord> initPage, QueryWrapper<AlarmRecord> initWrapper);

    ResultMessage alertsReceived(Map<String, Object> map);
}