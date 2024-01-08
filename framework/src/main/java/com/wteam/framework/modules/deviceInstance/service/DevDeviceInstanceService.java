package com.wteam.framework.modules.deviceInstance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;

/**
 * 设备信息表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-25
 */
public interface DevDeviceInstanceService extends IService<DevDeviceInstance> {

    Boolean enterpSave(String deviceId);

}