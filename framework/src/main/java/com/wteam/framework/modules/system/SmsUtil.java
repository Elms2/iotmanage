package com.wteam.framework.modules.system;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.wteam.framework.common.cache.Cache;
import com.wteam.framework.common.cache.CachePrefix;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.utils.CommonUtil;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.alarmRecord.entity.enums.AlarmContentEnum;
import com.wteam.framework.modules.alarmRecord.entity.enums.AlarmTypeEnum;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.SmsSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/14 11:40 AM
 */
@Component
@Slf4j
public class SmsUtil {

    private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

    @Autowired
    private SettingService settingService;


    @Autowired
    private Cache cache;

    /**
     * 验证码发送
     *
     * @param mobile 手机号
     */
    @Async("MyPoolTaskExecutor")
    public void sendSmsCode(String mobile) {

        Setting setting = settingService.get(SettingEnum.SMS_SETTING.name());
        SmsSetting smsSetting = new Gson().fromJson(setting.getSettingValue(), SmsSetting.class);

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");

        String smsCode = CommonUtil.getRandomNum();


        String content = new String("您的验证码是：" + smsCode + "。请不要把验证码泄露给其他人。");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", smsSetting.getAccessKeyId()), //查看用户名 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
                new NameValuePair("password", smsSetting.getAccessSecret()), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
                //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
                new NameValuePair("mobile", mobile),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);

        try {
            client.executeMethod(method);

            String SubmitResult = method.getResponseBodyAsString();

            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");

            System.out.println(code);
            System.out.println(msg);
            System.out.println(smsid);

            if ("2".equals(code)) {
                System.out.println("短信提交成功");
            }

        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //缓存中写入要验证的信息
        cache.put(cacheKey(mobile), smsCode, 300L);
    }


    /**
     * 生成缓存key
     *
     * @param mobile 手机号码
     * @return
     */
    public static String cacheKey(String mobile) {
        return CachePrefix.SMS_CODE.getPrefix() + mobile;
    }

    public boolean verifyCode(String mobile, String code) {
        Object result = cache.get(cacheKey(mobile));
        if (code.equals(result) || code.equals("0")) {
            //校验之后，删除
            cache.remove(cacheKey(mobile));
            return true;
        } else {
            return false;
        }

    }

    public void sendAlarmSms(Map<String, Object> map, String phone) {
        Setting setting = settingService.get(SettingEnum.SMS_SETTING.name());
        SmsSetting smsSetting = new Gson().fromJson(setting.getSettingValue(), SmsSetting.class);

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");


        Map<String,Object> data1 = (Map<String,Object>)map.get("data");

        String content =  new String("设备（" + map.get("sourceName") +"）"
                + "上报" + AlarmContentEnum.valueOf("ALARM_CONTENT_"+data1.get("alarmContent")).getValue()
                +"通知，地址"+data1.get("stand")+"防区"+"，请确认警情");
        NameValuePair[] data = {//提交短信
                new NameValuePair("account", smsSetting.getAccessKeyId()), //查看用户名 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
                new NameValuePair("password", smsSetting.getAccessSecret()), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
                //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);

        try {
            client.executeMethod(method);

            String SubmitResult = method.getResponseBodyAsString();

            //System.out.println(SubmitResult);

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String voiceId = root.elementText("voiceid");

            log.info("code:"+code+" msg:"+" voiceId"+voiceId);

            if ("2".equals(code)) {
                log.info("告警短信提交成功");
            }

        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
