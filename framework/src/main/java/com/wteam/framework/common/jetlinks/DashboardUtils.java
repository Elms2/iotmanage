package com.wteam.framework.common.jetlinks;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.google.gson.JsonObject;
import com.wteam.framework.common.utils.DateUtil;
import com.wteam.framework.common.utils.HttpUtils;
import com.wteam.framework.modules.entDevice.entity.dto.Dashboard;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.dto.JetlinksSetting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : lsllsl
 * @version : [v1.0]
 * @className : DashboardUtils
 * @description : [描述说明该类的功能]
 * @createTime : [2023/2/7 11:36]
 * @updateUser : [LiuYanQiang]
 * @updateTime : [2023/2/7 11:36]
 * @updateRemark : [描述说明本次修改内容]
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DashboardUtils {

    @Autowired
    private SettingService settingService;

    @Autowired
    private EntDevService entDevService;

    private String getBaseUrl() {
        Setting setting = settingService.get(SettingEnum.JETLINKS_SETTING.name());
        JetlinksSetting jetLink = JSONUtil.toBean(setting.getSettingValue(), JetlinksSetting.class);
        String baseUrl = jetLink.getBaseUrl();
        return baseUrl;
    }
    public JSONObject getSameMonth(){
        ArrayList<Object> list = new ArrayList<>();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("format","yyyy-MM-dd");
        stringStringHashMap.put("time","1d");


        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        Date date = DateUtil.endOfDate();
        Date startDate = DateUtils.addDays(date, -31);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end = simpleDateFormat.format(date);
        String start = simpleDateFormat.format(startDate);
        stringStringHashMap1.put("format","yyyy-MM-dd");
        stringStringHashMap1.put("from",start);
        stringStringHashMap1.put("limit","30");
        stringStringHashMap1.put("time","1d");
        stringStringHashMap1.put("to",end);

        HashMap<String, String> stringStringHashMap2 = new HashMap<>();
        Date date2 = DateUtil.endOfDate();
        Date startDate2 = DateUtils.addDays(date, -7);
        String end2 = simpleDateFormat.format(date2);
        String start2 = simpleDateFormat.format(startDate2);
        stringStringHashMap2.put("format","yyyy-MM-dd");
        stringStringHashMap2.put("from",start2);
        stringStringHashMap2.put("time","1M");
        stringStringHashMap2.put("to",end2);

        list.add(new MessageDto("device","agg","sameDay","quantity","message",stringStringHashMap));
        list.add(new MessageDto("device","agg","sameMonth","quantity","message",stringStringHashMap1));
        list.add(new MessageDto("device","agg","month","quantity","message",stringStringHashMap2));

        //查询月消息量,天总数，月总数
        String url = getBaseUrl() + "/dashboard/_multi";
        String s = HttpUtils.doPostWithJson(url, list);
        Map map = JSONObject.parseObject(s, Map.class);
        List<Dashboard> result = (List<Dashboard>) map.get("result");
        Long value = result.get(0).getData().getValue();
//        for (Dashboard dashboard : result) {
//
//        }
        JSONObject jsonObject = JSON.parseObject(s);
        return jsonObject;
    }


    public JSONObject getSameDay(){
        ArrayList<Object> list = new ArrayList<>();
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end = simpleDateFormat.format(date);
        stringStringHashMap1.put("format","HH时mm分");
        stringStringHashMap1.put("from",DateUtil.getTimeAferOne());
        stringStringHashMap1.put("limit","60");
        stringStringHashMap1.put("time","1m");
        stringStringHashMap1.put("to",end);



        list.add(new MessageDto("gatewayMonitor","agg","sameDay","received_message","deviceGateway",stringStringHashMap1));

        //查询月消息量,天总数，月总数
        String url = getBaseUrl() + "/dashboard/_multi";
        String s1 = JSONObject.toJSONString(list);
        String s = HttpUtils.doPostWithJson(url, list);
        JSONObject jsonObject = JSON.parseObject(s);
        return jsonObject;
    }

}
