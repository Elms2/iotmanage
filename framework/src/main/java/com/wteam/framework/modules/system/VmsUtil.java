package com.wteam.framework.modules.system;

import com.google.gson.Gson;
import com.wteam.framework.common.cache.Cache;
import com.wteam.framework.common.utils.CommonUtil;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.alarmRecord.entity.enums.AlarmContentEnum;
import com.wteam.framework.modules.alarmRecord.entity.enums.AlarmTypeEnum;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.VmsSetting;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : VmsUtil
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/5 12:13]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/5 12:13]
 * @updateRemark : [描述说明本次修改内容]
 */
@Component
@Slf4j
public class VmsUtil {
    private static String url = "http://api.vm.ihuyi.com/webservice/voice.php?method=Submit";

    
    @Autowired
    private SettingService settingService;


//    @Async("MyPoolTaskExecutor")
//    public void sendAlarmVms(Map<String, Object> map) {
//        //查询设备管理员
//        List<String> enIds = entDevService.query().eq("dev_id", map.get("deviceId")).list().stream().map(den -> {
//            return den.getEnId();
//        }).collect(Collectors.toList());
//
//
//        //存在企业
//        Admin one = adminService.query().eq("platform_flag", 1).in("id", enIds).one();
//
//        if(null==one){
//            //不存在企业发送消息给厂商
//            return;
//        }
//        List<Member> list = memberService.query().eq("role", 1).eq("en_id", one.getId()).list();
//        for (Member member : list) {
//            send(map,member.getPhone());
//        }
//
//    }

    public void sendAlarmSms(Map<String, Object> map, String phone) {
        Setting setting = settingService.get(SettingEnum.VMS_SETTING.name());
        VmsSetting vmsSetting = new Gson().fromJson(setting.getSettingValue(), VmsSetting.class);
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);

        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        Map<String,Object> data1 = (Map<String,Object>)map.get("data");

        String content =  new String("设备（" + map.get("sourceName") +"）"
                + "上报" + AlarmContentEnum.valueOf("ALARM_CONTENT_"+data1.get("alarmContent")).getValue()
                +"通知，地址"+data1.get("stand")+"防区"+"，请确认警情");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", vmsSetting.getAccessKeyId()), //查看用户名 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
                new NameValuePair("password", vmsSetting.getAccessSecret()), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
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
                log.info("告警语音发送成功");
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
