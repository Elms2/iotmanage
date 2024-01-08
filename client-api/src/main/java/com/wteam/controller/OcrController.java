package com.wteam.controller;

import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.utils.IpHelper;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.system.OcrUtil;
import com.wteam.framework.modules.system.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 短信验证码接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
@RequestMapping("/common/ocr")
public class OcrController {


    @Autowired
    private OcrUtil orcUtil;

    @Autowired
    private IpHelper ipHelper;


    @GetMapping
    public ResultMessage getSmsCode(@RequestParam String image) {

        orcUtil.ocrScanner(image);
        return ResultUtil.success();
    }


    @GetMapping("qr")
    public ResultMessage QRCode(@RequestParam String image) {


        return ResultUtil.data(orcUtil.ocrQRScanner(image));
    }

    @GetMapping("t")
    public ResultMessage dasda(@RequestParam String url) {
        String ipCity = ipHelper.getIpCity(url);
        return ResultUtil.data(ipCity);
    }


}
