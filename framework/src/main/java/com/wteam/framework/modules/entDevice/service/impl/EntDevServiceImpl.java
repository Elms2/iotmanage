package com.wteam.framework.modules.entDevice.service.impl;


import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.modules.entDevice.entity.dto.Dashboard;
import com.wteam.framework.modules.entDevice.entity.dto.DashboardData;
import com.wteam.framework.common.jetlinks.DeviceUtils;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.mapper.EntDevMapper;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Service
public class EntDevServiceImpl extends ServiceImpl<EntDevMapper, EntDev> implements EntDevService {

    @Autowired
    private DeviceUtils deviceUtils;

    @Autowired
    private EntDevService entDevService;

    @Autowired
    private DevDeviceInstanceService devDeviceInstanceService;
    @Override
    public void industryDevice(Map<String, Object> map) {
        AuthUser user = UserContext.getCurrentUser();
        deviceUtils.create(map, user.getId());

    }

    @Override
    public List<Dashboard> selectDeviceCount() {
        //获取当前用户
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser.getRole() != UserEnums.MANAGER) {
            throw new ServiceException(ResultCode.ERROR);
        }
        ArrayList<Dashboard> dashboards = new ArrayList<>();
        Dashboard deviceCount = new Dashboard("deviceCount");
        Dashboard deviceOnlines = new Dashboard("deviceOnline");
        Dashboard deviceNotActives = new Dashboard("deviceNotActive");
        Date date = new Date();
        if(currentUser.getIsSuper()){

            deviceCount.setData(new DashboardData(devDeviceInstanceService.count(),date,String.valueOf(date.getTime())));
            deviceOnlines.setData(new DashboardData(devDeviceInstanceService.query().eq("state", "online").count(),date,String.valueOf(date.getTime())));
            deviceNotActives.setData(new DashboardData(devDeviceInstanceService.query().eq("state", "notActive").count(),date,String.valueOf(date.getTime())));
            dashboards.add(deviceCount);
            dashboards.add(deviceOnlines);
            dashboards.add(deviceNotActives);
            return dashboards;
        }
        List<String> ids = entDevService.query().eq("en_id", currentUser.getId()).list().stream().map(den -> {
            return den.getDevId();
        }).collect(Collectors.toList());
        deviceCount.setData(new DashboardData(ids.stream().count(),date,String.valueOf(date.getTime())));
        deviceOnlines.setData(new DashboardData(devDeviceInstanceService.query().eq("state", "online").in("source_id", ids).count(),date,String.valueOf(date.getTime())));
        deviceNotActives.setData(new DashboardData(devDeviceInstanceService.query().eq("state", "notActive").in("source_id", ids).count(),date,String.valueOf(date.getTime())));
        dashboards.add(deviceCount);
        dashboards.add(deviceOnlines);
        dashboards.add(deviceNotActives);
        return dashboards;
    }
}