package com.wteam.framework.common.jetlinks;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.utils.HttpUtils;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.entPro.service.EntProService;
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
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/18 3:00 PM
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProductsUtils {

    @Autowired
    private EntProService entProService;


    @Autowired
    private SettingService settingService;

    private String getBaseUrl() {
        Setting setting = settingService.get(SettingEnum.JETLINKS_SETTING.name());
        JetlinksSetting jetLink = JSONUtil.toBean(setting.getSettingValue(), JetlinksSetting.class);
        String baseUrl = jetLink.getBaseUrl();
        return baseUrl;
    }

    /**
     * 厂商添加产品
     * <p>
     * /**
     * classifiedId
     * :
     * "-45-"
     * classifiedName
     * :
     * "智能工业"
     * describe
     * :
     * "fsfsfsf"
     * deviceType
     * :
     * "device"
     * id
     * :
     * "ff"
     * name
     * :
     * "sdfsfs"
     * photoUrl
     * :
     * "/images/device-product.png"
     */

    public ResultMessage create(Map<String, Object> map, String enId) {

        //发送url
        String url = getBaseUrl() + "/device-product";
        //添加产品
        JSONObject entries = this.doPostWithJson(url, map);
        String result = entries.getStr("result");
        JSONObject jsonResult = new JSONObject(result);
        String id = jsonResult.getStr("id");
        EntPro entPro = new EntPro();
        entPro.setProId(id);
        entPro.setEnId(enId);

        return entProService.save(entPro) ? ResultUtil.success() : ResultUtil.error(ResultCode.PARAMS_ERROR);

    }


    /**
     * 厂商查看产品
     * @return
     */
    public JSONObject searchProduct(String enId) {
        Map<String, Object> map = new HashMap<>(5);

        if (ObjectUtil.isNull(enId)){
            //发送url
            String url = getBaseUrl() + "/device-product/_query";
            //查询产品
            JSONObject entries = this.doPostWithJson(url,map);

            return entries;
        }

        List<EntPro> entProList = entProService.query().eq("en_id", enId).list();

        if (CollUtil.isEmpty(entProList)) {
            throw new ServiceException(ResultCode.PRODUCTS_NULL);
        }

        List<Terms> termsList = new ArrayList<>();
        for (EntPro entPro : entProList) {
            Terms terms = new Terms();
            terms.setTermType("eq");
            terms.setColumn("id");
            terms.setType("or");
            terms.setValue(entPro.getProId());
            termsList.add(terms);
        }
        map.put("terms",termsList);
        //发送url
        String url = getBaseUrl() + "/device-product/_query";
        //查询产品
        JSONObject entries = this.doPostWithJson(url, map);

        return entries;

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
//        log.info("请求结果：" + content);
        JSONObject jsonObject = new JSONObject(content);
        if (("success").equals(jsonObject.get("message").toString())) {
            return jsonObject;
        } else {
            throw new ServiceException(ResultCode.JETLINKS_ERROR);
        }
    }
}
