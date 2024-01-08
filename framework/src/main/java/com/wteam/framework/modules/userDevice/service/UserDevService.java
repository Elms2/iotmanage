package com.wteam.framework.modules.userDevice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.userDevice.entity.dos.UserDev;

import java.util.Map;

/**
 * 员工设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
public interface UserDevService extends IService<UserDev> {

    ResultMessage create(Map<String, Object> var1);

    IPage<DevDeviceInstance> all(String enId);

    IPage<DevDeviceInstance> MemberAll();

    IPage<DevDeviceInstance> AdminAll();


    IPage<DevDeviceInstance> search(DevDeviceInstance instance);

    IPage<DevDeviceInstance> AdminAll(String id);
}