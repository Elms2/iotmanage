package com.wteam.controller;

import cn.hutool.core.text.CharSequenceUtil;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wteam.framework.common.cache.Cache;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;

import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.utils.Base64DecodeMultipartFile;
import com.wteam.framework.common.utils.BeanUtil;
import com.wteam.framework.common.utils.CommonUtil;
import com.wteam.framework.common.utils.StringUtils;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.file.plugin.FilePluginFactory;
import com.wteam.framework.modules.file.service.FileService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文件上传接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@Slf4j
@RestController
@Api(tags = "文件")
@RequestMapping("/common/file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private FilePluginFactory filePluginFactory;
    @Autowired
    private Cache cache;

    @Autowired
    private EntInfoService entInfoService;

    @Autowired
    private MemberService memberService;


    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody File entity) {
        ///处理过了
        if (!("0").equals(entity.getState())) {
            entity.setState("0");
            return fileService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
        }
        return fileService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<File>> page(File entity,
                                           SearchVO searchVo,
                                           PageVO page) {

        QueryWrapper<File> wrapper = PageUtil.initWrapper(entity);
        if (ObjectUtil.isNotNull(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        wrapper.orderByDesc("create_time");
        IPage<File> data = fileService.page(PageUtil.initPage(page), wrapper);
        data.getRecords().forEach(x -> {
            EntInfo info = entInfoService.query().eq("en_id", x.getEnId()).one();
            //FIXME:直接enName不香吗
            if (ObjectUtil.isNull(info)) {
                EntInfo entInfo = new EntInfo();
                x.setEntInfo(entInfo);
            } else {
                x.setEntInfo(info);
            }

            x.setName(StringUtils.subStringLength(x.getName(), 21));


        });
        return ResultUtil.data(data);
    }


    @PostMapping("saveContent")
    public ResultMessage<Object> upload(@RequestBody File file) {

        AuthUser currentUser = UserContext.getCurrentUser();
        Member member = memberService.query().eq("id", currentUser.getId()).one();
        file.setEnId(member.getEnId());
        return fileService.save(file) ? ResultUtil.success() : ResultUtil.error(ResultCode.PARAMS_ERROR);

    }


    //素材记录
    @GetMapping("/log")
    @ApiOperation("信息")
    public ResultMessage log() {
        AuthUser currentUser = UserContext.getCurrentUser();
//        Member member = memberService.query().eq("id", currentUser.getId()).one();
        List<File> fileList = fileService.query().eq("owner_id", currentUser.getId()).orderByDesc("create_time").list();
//        if (null != fileList) {
//            return ResultUtil.data(fileList);
//        } else {
//            return ResultUtil.data("您还没有提交过素材~");
//        }
        //让世界少点if else 吧
        if (ObjectUtil.isNull(fileList)) {
            throw new ServiceException(ResultCode.FILE_LOG_EMPTY);
        }

        fileList.forEach(x -> {
            x.setName(StringUtils.subStringLength(x.getName(), 21));
        });
        return ResultUtil.data(fileList);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("信息")
    public ResultMessage get(@PathVariable("id") String id) {
        File data = fileService.getById(id);
        return ResultUtil.data(data);
    }


    @PostMapping("upload")
    public ResultMessage upload(MultipartFile file) {
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


    @PostMapping("uploadContent")
    public ResultMessage uploadContent(MultipartFile file) {
        AuthUser authUser = UserContext.getCurrentUser();
        String username = authUser.getUsername();
        //如果用户未登录，则无法上传图片
        if (null == authUser) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        if (null == file) {
            throw new ServiceException(ResultCode.FILE_NOT_EXIST_ERROR);
        }
        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        if (null == setting || CharSequenceUtil.isBlank(setting.getSettingValue())) {
            throw new ServiceException(ResultCode.OSS_NOT_EXIST);
        }

        String result = null;
        String fileKey = CommonUtil.rename(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File();
        try {
            InputStream inputStream = file.getInputStream();
            //上传至第三方云服务或服务器
            result = filePluginFactory.filePlugin().inputStreamUpload(inputStream, fileKey);
            newFile.setName(file.getOriginalFilename());
            newFile.setFileSize(file.getSize());
            newFile.setFileType(file.getContentType());
            newFile.setFileKey(fileKey);
            newFile.setUrl(result);
            newFile.setCreateBy(authUser.getUsername());
            newFile.setOwnerId(authUser.getId());
            newFile.setUrl(result);
            newFile.setOwnerName(username);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        }

        return ResultUtil.data(newFile);
    }


}
