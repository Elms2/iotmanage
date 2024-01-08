package com.wteam.controller;

import cn.hutool.json.JSONUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.system.SmsUtil;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.SystemInfoSetting;
import com.wteam.framework.modules.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 短信验证码接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
 @RequestMapping("/common/sys")
public class SystemController {



    @Autowired
    private SettingService settingService;


    @GetMapping("/info")
    public ResultMessage getInfo() {
        Setting setting = settingService.get("SYSTEMINFO_SETTING");
        return setting == null ?
                ResultUtil.data(new SystemInfoSetting()) :
                ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), SystemInfoSetting.class));
    }


}
