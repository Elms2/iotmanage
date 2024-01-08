package com.wteam.framework.common.jetlinks;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.ejlchina.okhttps.OkHttps;
import com.wteam.framework.common.enums.ClientTypeEnum;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.utils.HttpUtils;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.entPro.service.EntProService;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.JetlinksSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/18 3:00 PM
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DeviceUtils {


    @Autowired
    private EntDevService entDevService;

    @Autowired
    private EntInfoService entInfoService;


    @Autowired
    private SettingService settingService;

    @Autowired
    private DevDeviceInstanceService instanceService;

    private String getBaseUrl() {
        Setting setting = settingService.get(SettingEnum.JETLINKS_SETTING.name());
        JetlinksSetting jetLink = JSONUtil.toBean(setting.getSettingValue(), JetlinksSetting.class);
        String baseUrl = jetLink.getBaseUrl();
        return baseUrl;
    }

    /**
     * 添加设备
     */
    public ResultMessage create(Map<String, Object> map, String enId) {


        //发送url
        String url = getBaseUrl() + "/device-instance";
        String content = OkHttps.sync(url)
                .bodyType("application/json")
                .addBodyPara(map)
                .patch()
                .getBody()
                .toString();

        log.info("请求结果：" + content);
//        JSONObject jsonObject = new JSONObject(content);


        //添加
//        String result = jsonObject.getStr("result");
//        log.info("测试：" + content);
//        JSONObject jsonResult = new JSONObject(result);
        String id = map.get("id").toString();
        EntDev entPro = new EntDev();
        entPro.setDevId(id);
        entPro.setEnId(enId);

        EntInfo info = entInfoService.query().eq("en_id", enId).one();
        String deviceId = map.get("id").toString();
        DevDeviceInstance one = instanceService.query().eq("id", deviceId).one();
        one.setEntName(info.getEnName());
        //修改所属企业
        instanceService.updateById(one);


        return entDevService.save(entPro) ? ResultUtil.success() : ResultUtil.error(ResultCode.PARAMS_ERROR);

    }


    /**
     * 厂商查看产品
     */
    public ResultMessage searchProduct(String enId) {

        OkHttps.sync(getBaseUrl() + "/device-instance")
                .bodyType("application/json")
                .addBodyPara("opbChainId", 3)
                .patch()
                .getBody()
                .toString();
        return null;

    }


    /**
     * 请求jetlinks接口
     *
     * @param url 链接
     * @param map 参数
     * @return 返回内容
     */

    private JSONObject doPostWithJson(String url, Map map) {

        String content = HttpUtils.doPostWithJson(url, map);
        log.info("请求结果：" + content);
        JSONObject jsonObject = new JSONObject(content);
        if (("success").equals(jsonObject.get("message").toString())) {
            return jsonObject;
        } else {
            throw new ServiceException(ResultCode.JETLINKS_ERROR);
        }
    }
}
