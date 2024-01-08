package com.wteam.contorller.system;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.utils.CommonUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.file.plugin.FilePluginFactory;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/18 2:42 PM
 */
@RestController
@Api(tags = "管理端,系统设置接口")
@RequestMapping("/manager/file")
@Slf4j
public class FileManagerController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private FilePluginFactory filePluginFactory;






    @PostMapping("upload")
    public ResultMessage  upload(MultipartFile file) {
        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        if (setting == null || CharSequenceUtil.isBlank(setting.getSettingValue())) {
            throw new ServiceException(ResultCode.OSS_NOT_EXIST);
        }
        String result = null;
        String fileKey = CommonUtil.rename(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File();
        try {
            InputStream inputStream = file.getInputStream();
            //上传至第三方云服务或服务器
            result = filePluginFactory.filePlugin().inputStreamUpload(inputStream, fileKey);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        }

        return ResultUtil.data(result);
    }
}
