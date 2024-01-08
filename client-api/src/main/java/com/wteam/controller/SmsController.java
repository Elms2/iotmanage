package com.wteam.controller;

import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.system.SmsUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 短信验证码接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
@Api(tags = "短信验证码接口")
@RequestMapping("/common/sms")
public class SmsController {

    @Resource
    private SmsUtil smsUtil;


    @GetMapping("/{mobile}")
    public ResultMessage getSmsCode(@PathVariable String mobile) {

        smsUtil.sendSmsCode(mobile);
        return ResultUtil.success(ResultCode.VERIFICATION_SEND_SUCCESS);
    }





}
