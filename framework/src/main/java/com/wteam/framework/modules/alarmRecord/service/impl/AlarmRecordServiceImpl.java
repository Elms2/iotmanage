package com.wteam.framework.modules.alarmRecord.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.alarmRecord.entity.dos.AlarmRecord;
import com.wteam.framework.modules.alarmRecord.mapper.AlarmRecordMapper;
import com.wteam.framework.modules.alarmRecord.service.AlarmRecordService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.SmsUtil;
import com.wteam.framework.modules.system.VmsUtil;
import com.wteam.framework.modules.userDevice.service.UserDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
@Service
public class AlarmRecordServiceImpl extends ServiceImpl<AlarmRecordMapper, AlarmRecord> implements AlarmRecordService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private EntDevService entDevService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserDevService userDevService;

    @Resource
    private VmsUtil vmsUtil;

    @Resource
    private SmsUtil smsUtil;

    @Override
    public IPage<AlarmRecord> rePage(Page<AlarmRecord> initPage, QueryWrapper<AlarmRecord> initWrapper) {
        //获取当前用户
        AuthUser currentUser = UserContext.getCurrentUser();
        IPage<AlarmRecord> data=null;
        String enId = currentUser.getId();
        if (currentUser.getRole() == UserEnums.MANAGER) {
            Admin admin = adminService.query().eq("id", currentUser.getId()).one();
            //超级管理员
            if(admin.getIsSuper()){
               return data = this.page(initPage, initWrapper);
            }
            enId = admin.getId();
        }

        if (currentUser.getRole() == UserEnums.MEMBER) {
            Member member = memberService.query().eq("id", currentUser.getId()).one();
            //获取企业id
            enId=member.getEnId();
        }

        List<String> en_id = entDevService.query().eq("en_id", enId).list().stream().map(den -> {
            return den.getDevId();
        }).collect(Collectors.toList());
        QueryWrapper<AlarmRecord> source_id = initWrapper.in("source_id", en_id);
        data = this.page(initPage, source_id);
        return data;
    }

    @Override
    public ResultMessage alertsReceived(Map<String, Object> map) {
        sendAlarmSms(map);
        sendAlarmVms(map);
        return ResultUtil.success();
    }


    @Async("MyPoolTaskExecutor")
    public void sendAlarmSms(Map<String, Object> map) {
        //查询设备管理员
        List<String> enIds = entDevService.query().eq("dev_id", map.get("deviceId")).list().stream().map(den -> {
            return den.getEnId();
        }).collect(Collectors.toList());


        //存在企业
        Admin one = adminService.query().eq("platform_flag", 1).in("id", enIds).one();

        if(null==one){
            //不存在企业发送消息给厂商
            return;
        }
        List<Member> list = memberService.query().eq("role", 1).eq("en_id", one.getId()).list();
        for (Member member : list) {
            smsUtil.sendAlarmSms(map,member.getPhone());
        }
    }

    @Async("MyPoolTaskExecutor")
    public void sendAlarmVms(Map<String, Object> map) {
        //查询设备管理员
        List<String> enIds = entDevService.query().eq("dev_id", map.get("deviceId")).list().stream().map(den -> {
            return den.getEnId();
        }).collect(Collectors.toList());


        //存在企业
        Admin one = adminService.query().eq("platform_flag", 1).in("id", enIds).one();

        if(null==one){
            //不存在企业发送消息给厂商
            return;
        }
        List<Member> list = memberService.query().eq("role", 1).eq("en_id", one.getId()).list();
        for (Member member : list) {
            vmsUtil.sendAlarmSms(map,member.getPhone());
        }

    }
}