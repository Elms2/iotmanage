package com.wteam.contorller.system;

import cn.hutool.json.JSONUtil;

import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.system.entity.dos.Setting;

import com.wteam.framework.modules.system.entity.dto.*;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 管理端,系统设置接口
 *
 * @author Chopper
 * @since 2020/11/26 15:53
 */
@RestController
@Api(tags = "管理端,系统设置接口")
@RequestMapping("/manager/setting")
public class SettingManagerController {
    @Autowired
    private SettingService settingService;


    @ApiOperation(value = "更新配置")
    @PutMapping(value = "/put/{key}")
    public ResultMessage saveConfig(@PathVariable String key, @RequestBody String configValue) {
        SettingEnum settingEnum = SettingEnum.valueOf(key);
        //获取系统配置
        Setting setting = settingService.getById(settingEnum.name());
        if (setting == null) {
            setting = new Setting();
            setting.setId(settingEnum.name());
        }
        //特殊配置过滤

        setting.setSettingValue(configValue);
        settingService.saveUpdate(setting);
        return ResultUtil.success();
    }


    @ApiOperation(value = "查看配置")
    @GetMapping(value = "/get/{key}")

    public ResultMessage settingGet(@PathVariable String key) {
        return createSetting(key);
    }


    /**
     * 获取表单
     * 这里主要包含一个配置对象为空，导致转换异常问题的处理，解决配置项增加减少，带来的系统异常，无法直接配置
     *
     * @param key
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private ResultMessage createSetting(String key) {
        SettingEnum settingEnum = SettingEnum.valueOf(key);
        Setting setting = settingService.get(key);

        switch (settingEnum) {

            case OSS_SETTING:
                return setting == null ?
                        ResultUtil.data(new OssSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), OssSetting.class));
            case SMS_SETTING:
                return setting == null ?
                        ResultUtil.data(new SmsSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), SmsSetting.class));
            case SYSTEMINFO_SETTING:
                return setting == null ?
                        ResultUtil.data(new SystemInfoSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), SystemInfoSetting.class));
            case TENCENT_SETTING:
                return setting == null ?
                        ResultUtil.data(new TencentSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), TencentSetting.class));
            case VMS_SETTING:
                return setting == null ?
                        ResultUtil.data(new TencentSetting()) :
                        ResultUtil.data(JSONUtil.toBean(setting.getSettingValue(), VmsSetting.class));

            default:
                throw new ServiceException(ResultCode.SETTING_NOT_TO_SET);
        }
    }
}
