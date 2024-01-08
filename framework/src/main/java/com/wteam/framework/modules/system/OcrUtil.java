package com.wteam.framework.modules.system;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralAccurateOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.QrcodeOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.QrcodeOCRResponse;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.TencentSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/15 5:18 PM
 */
@Component
public class OcrUtil {

    @Autowired
    private SettingService settingService;


    public void ocrScanner(String url) {

        Setting setting = settingService.get(SettingEnum.TENCENT_SETTING.name());

        TencentSetting tencentSetting = JSONUtil.toBean(setting.getSettingValue(), TencentSetting.class);
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(tencentSetting.getSecretId(), tencentSetting.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);

            // 实例化一个请求对象,每个接口都会对应一个request对象
            GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();

            req.setImageUrl(url);
            // 返回的resp是一个GeneralAccurateOCRResponse的实例，与请求对象对应
            GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);
            // 输出json格式的字符串回包
            System.out.println(GeneralAccurateOCRResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }


    public String ocrQRScanner(String url) {

        Setting setting = settingService.get(SettingEnum.TENCENT_SETTING.name());

        TencentSetting tencentSetting = JSONUtil.toBean(setting.getSettingValue(), TencentSetting.class);
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(tencentSetting.getSecretId(), tencentSetting.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            QrcodeOCRRequest req = new QrcodeOCRRequest();
//            req.setImageBase64("dasda");
            req.setImageUrl(url);
            // 返回的resp是一个QrcodeOCRResponse的实例，与请求对象对应
            QrcodeOCRResponse resp = client.QrcodeOCR(req);
            // 输出json格式的字符串回包
            String s = QrcodeOCRResponse.toJsonString(resp);

            JSONObject jsonObject = new JSONObject(s);
            String codeResults = jsonObject.getStr("CodeResults");
            JSONArray array = JSON.parseArray(codeResults);
            String obj = array.get(0).toString();
            JSONObject jsObj = new JSONObject(obj);
            String id = jsObj.getStr("Url");
            System.out.println(id);
            return id;
//            System.out.println();
        } catch (TencentCloudSDKException e) {
            throw new ServiceException(ResultCode.QRCODE_NULL);
        }
    }
}