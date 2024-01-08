package com.wteam.controller.device;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.jetlinks.DeviceUtils;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.OcrUtil;
import com.wteam.framework.modules.userDevice.service.UserDevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 短信验证码接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DevDeviceInstanceService instanceService;

    @Autowired
    private UserDevService userDevService;
    @Autowired
    private MemberService memberService;

    /**
     * 企业添加设备
     *
     * @return
     */
    @PostMapping("create/{deviceId}")
    public ResultMessage create(@PathVariable String deviceId) {

        return instanceService.enterpSave(deviceId) ? ResultUtil.success() : ResultUtil.error(ResultCode.PARAMS_ERROR);
    }

    /**
     * 设备列表
     *
     * @return
     */
    @PostMapping({"/enterp/all"})
    public ResultMessage all() {
        IPage<DevDeviceInstance> all = userDevService.MemberAll();
        return ResultUtil.data(all);
    }


}
