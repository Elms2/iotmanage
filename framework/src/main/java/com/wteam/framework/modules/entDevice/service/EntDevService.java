package com.wteam.framework.modules.entDevice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.modules.entDevice.entity.dto.Dashboard;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;

import java.util.List;
import java.util.Map;

/**
 * 企业设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
public interface EntDevService extends IService<EntDev> {

    void industryDevice(Map<String, Object> map);
    /**
     * @method
     * @description 获取设备总数，设备在线数。设备未激活数
     * @date: 2023/2/7 13:49
     * @author: lsllsl
     * @return * @return Long
     */
    List<Dashboard> selectDeviceCount();
}