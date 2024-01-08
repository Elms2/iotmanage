package com.wteam.framework.modules.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.mapper.SettingMapper;
import com.wteam.framework.modules.system.service.SettingService;
import org.springframework.stereotype.Service;

/**
 * 配置业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:52 下午
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

    @Override
    public Setting get(String key) {
        return this.getById(key);
    }

    @Override
    public boolean saveUpdate(Setting setting) {
        return this.saveOrUpdate(setting);
    }
}