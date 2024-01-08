package com.wteam.framework.modules.userDevice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.jetlinks.DeviceUtils;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.userDevice.entity.dos.UserDev;
import com.wteam.framework.modules.userDevice.mapper.UserDevMapper;
import com.wteam.framework.modules.userDevice.service.UserDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 员工设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Service
public class UserDevServiceImpl extends ServiceImpl<UserDevMapper, UserDev> implements UserDevService {
    @Autowired
    private DeviceUtils deviceUtils;
    @Autowired
    private EntDevService entDevService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private DevDeviceInstanceService instanceService;

    @Autowired
    private EntInfoService entInfoService;
    @Override
    public ResultMessage create(Map<String, Object> map) {
        AuthUser user = UserContext.getCurrentUser();
        Member member = (Member) ((QueryChainWrapper) this.memberService.query().eq("id", user.getId())).one();
        if (!"1".equals(member.getRoleId())) {
            throw new ServiceException(ResultCode.ERROR);
        } else {
            UserDev userDev = new UserDev();
            userDev.setDevId("dasda");
            userDev.setUserId(user.getId());
            return this.save(userDev) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
        }
    }

    @Override
    public IPage<DevDeviceInstance> all(String enId) {

        EntInfo info = entInfoService.query().eq("en_id", enId).one();

        List<EntDev> devs = this.entDevService.list(new QueryWrapper<EntDev>().eq("en_id", enId));

        if (CollUtil.isEmpty(devs)) {
            throw new ServiceException(ResultCode.PRODUCTS_NULL);
        }

        PageVO pageVO = new PageVO();
        QueryWrapper<DevDeviceInstance> wrapper = new QueryWrapper();
        Iterator var7 = devs.iterator();

        while (var7.hasNext()) {
            EntDev dev = (EntDev) var7.next();
            ((QueryWrapper) wrapper.eq("id", dev.getDevId())).or();
        }

        wrapper.orderByAsc("sort");

        IPage<DevDeviceInstance> data = this.instanceService.page(PageUtil.initPage(pageVO), wrapper);
        data.getRecords().forEach(x->{
            x.setEntName(info.getEnName());
        });

        return data;
    }

    @Override
    public IPage<DevDeviceInstance> MemberAll() {
        AuthUser user = UserContext.getCurrentUser();
        Member member = (Member) ((QueryChainWrapper) this.memberService.query().eq("id", user.getId())).one();
        String enId = member.getEnId();
        return all(enId);
    }

    @Override
    public IPage<DevDeviceInstance> AdminAll() {
        AuthUser user = UserContext.getCurrentUser();
        return all(user.getId());
    }

    @Override
    public IPage<DevDeviceInstance> search(DevDeviceInstance instance) {

        AuthUser user = UserContext.getCurrentUser();

        IPage<DevDeviceInstance> all = this.all(user.getId());
        List<DevDeviceInstance> records = all.getRecords();
        if (ObjectUtil.isNotNull(instance.getState())) {
            List<DevDeviceInstance> collect = records.stream()
                    .filter(x -> {
                        return x.getState().equals(instance.getState());
                    }).collect(Collectors.toList());
            PageUtil.convertPage(all,collect);
        }

        if (ObjectUtil.isNotNull(instance.getFirmwareVersion())) {
            List<DevDeviceInstance> collect = records.stream()
                    .filter(x -> {
                        return x.getState().equals(instance.getState());
                    }).collect(Collectors.toList());
            PageUtil.convertPage(all,collect);
        }

        if (ObjectUtil.isNotNull(instance.getLocation())) {
            List<DevDeviceInstance> collect = records.stream()
                    .filter(x -> {
                        return x.getState().equals(instance.getState());
                    }).collect(Collectors.toList());
            PageUtil.convertPage(all,collect);
        }

        if (ObjectUtil.isNotNull(instance.getProductId())) {
            List<DevDeviceInstance> collect = records.stream()
                    .filter(x -> {
                        return x.getState().equals(instance.getState());
                    }).collect(Collectors.toList());
            PageUtil.convertPage(all,collect);
        }

        if (ObjectUtil.isNotNull(instance.getName())) {
            List<DevDeviceInstance> collect = records.stream()
                    .filter(x -> {
                        return x.getState().equals(instance.getState());
                    }).collect(Collectors.toList());
            PageUtil.convertPage(all,collect);
        }
        return null;

    }

    @Override
    public IPage<DevDeviceInstance> AdminAll(String id) {
        return all(id);
    }


}