package com.wteam.framework.modules.deviceInstance.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.mapper.DevDeviceInstanceMapper;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 设备信息表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-25
 */
@Service
public class DevDeviceInstanceServiceImpl extends ServiceImpl<DevDeviceInstanceMapper, DevDeviceInstance> implements DevDeviceInstanceService {

    @Autowired
    private EntDevService entDevService;

    @Autowired
    private MemberService memberService;

    @Override
    public Boolean enterpSave(String deviceId) {

        AuthUser currentUser = UserContext.getCurrentUser();
        if (null == currentUser) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        Member member = memberService.query().eq("id", currentUser.getId()).one();

        DevDeviceInstance one = this.query().eq("id", deviceId).one();
        if (ObjectUtil.isNull(one)) {
            throw new ServiceException(ResultCode.DEVICE_NULL);
        }

        //查询是否已经添加过
        EntDev dev = entDevService.query().eq("en_id", member.getEnId()).eq("dev_id", deviceId).one();
        if (ObjectUtil.isNotNull(dev)) {
            throw new ServiceException(ResultCode.DEVICE_EXIST);
        }

        //查询自己是否属于该厂商。。。

        //添加中间表
        EntDev entDev = new EntDev();
        entDev.setDevId(deviceId);
        entDev.setEnId(member.getEnId());
        return entDevService.save(entDev);
    }
}