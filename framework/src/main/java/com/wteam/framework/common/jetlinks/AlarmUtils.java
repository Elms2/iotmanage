package com.wteam.framework.common.jetlinks;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.utils.HttpUtils;
import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.JetlinksSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : AlarmUtils
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/3 12:27]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/3 12:27]
 * @updateRemark : [描述说明本次修改内容]
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AlarmUtils {

    @Autowired
    private SettingService settingService;

    private String getBaseUrl() {
        Setting setting = settingService.get(SettingEnum.JETLINKS_SETTING.name());
        JetlinksSetting jetLink = JSONUtil.toBean(setting.getSettingValue(), JetlinksSetting.class);
        String baseUrl = jetLink.getBaseUrl();
        return baseUrl;
    }

    /**
     * @method
     * @description 查询告警记录
     * @date: 2023/2/3 12:30
     * @author: lsllsl
     * @return * @return JSONObject
     */
    public JSONObject searchProduct(Map<String,String> map) {
        //处理查询条件
        //发送url
        String url = getBaseUrl() + "/device-instance";
        JSONObject entries = this.doPostWithJson(url, map);

        return entries;

    }

    private JSONObject doPostWithJson(String url, Map map) {

        String content = HttpUtils.doPostWithJson(url, map);
//        log.info("请求结果：" + content);
        JSONObject jsonObject = new JSONObject(content);
        if (("success").equals(jsonObject.get("message").toString())) {
            return jsonObject;
        } else {
            throw new ServiceException(ResultCode.JETLINKS_ERROR);
        }
    }
}
