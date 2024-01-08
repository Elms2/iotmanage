package com.wteam.framework.modules.file.plugin;

import cn.hutool.json.JSONUtil;


import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.modules.file.entity.enums.OssEnum;
import com.wteam.framework.modules.file.plugin.impl.AliFilePlugin;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.OssSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文件服务抽象工厂 直接返回操作类
 *
 * @author Chopper
 * @version v1.0
 * 2022-06-06 11:35
 */
@Component
public class FilePluginFactory {


    @Autowired
    private SettingService settingService;


    /**
     * 获取oss client
     *
     * @return
     */
    public FilePlugin filePlugin() {

        OssSetting ossSetting = null;
        try {
            Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());

            ossSetting = JSONUtil.toBean(setting.getSettingValue(), OssSetting.class);

            return new AliFilePlugin(ossSetting,settingService);

        } catch (Exception e) {
            throw new ServiceException();
        }
    }


}
