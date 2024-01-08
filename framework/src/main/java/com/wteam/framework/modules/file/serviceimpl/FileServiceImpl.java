package com.wteam.framework.modules.file.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;

 import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.file.mapper.FileMapper;
import com.wteam.framework.modules.file.plugin.FilePluginFactory;
import com.wteam.framework.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理业务层实现
 *
 * @author Chopper
 * @since 2020/11/26 17:50
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private FilePluginFactory filePluginFactory;

    @Override
    public void batchDelete(List<String> ids) {

        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(File::getId, ids);

        List<File> files = this.list(queryWrapper);
        List<String> keys = new ArrayList<>();
        files.forEach(item -> keys.add(item.getFileKey()));
        filePluginFactory.filePlugin().deleteFile(keys);
        this.remove(queryWrapper);
    }

//    @Override
//    public void batchDelete(List<String> ids, AuthUser authUser) {
//        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(File::getId, ids);
//
//        queryWrapper.eq(File::getUserEnums, authUser.getRole().name());
//        //操作图片属性判定
//        switch (authUser.getRole()) {
//            case MEMBER:
//                queryWrapper.eq(File::getOwnerId, authUser.getId());
//                break;
//
//                break;
//            case MANAGER:
//                break;
//            default:
//                throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
//        }
//        List<File> files = this.list(queryWrapper);
//        List<String> keys = new ArrayList<>();
//        files.forEach(item -> keys.add(item.getFileKey()));
//        filePluginFactory.filePlugin().deleteFile(keys);
//        this.remove(queryWrapper);
//    }

//    @Override
//    public IPage<File> customerPage(File file, SearchVO searchVO, PageVO pageVo) {
//        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(CharSequenceUtil.isNotEmpty(file.getName()), File::getName, file.getName())
//                .like(CharSequenceUtil.isNotEmpty(file.getFileKey()), File::getFileKey, file.getFileKey())
//                .like(CharSequenceUtil.isNotEmpty(file.getFileType()), File::getFileType, file.getFileType())
//                .between(CharSequenceUtil.isNotEmpty(searchVO.getStartDate()) && CharSequenceUtil.isNotEmpty(searchVO.getEndDate()),
//                        File::getCreateTime, searchVO.getStartDate(), searchVO.getEndDate());
//        return this.page(PageUtil.initPage(pageVo), queryWrapper);
//    }
//
//    @Override
//    public IPage<File> customerPageOwner(FileOwnerDTO ownerDTO, File file, SearchVO searchVO, PageVO pageVo) {
//        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(CharSequenceUtil.isNotEmpty(ownerDTO.getOwnerId()), File::getOwnerId, ownerDTO.getOwnerId())
//                .eq(File::getUserEnums, ownerDTO.getUserEnums())
//                .like(CharSequenceUtil.isNotEmpty(file.getName()), File::getName, file.getName())
//                .like(CharSequenceUtil.isNotEmpty(file.getFileKey()), File::getFileKey, file.getFileKey())
//                .like(CharSequenceUtil.isNotEmpty(file.getFileType()), File::getFileType, file.getFileType())
//                .between(CharSequenceUtil.isNotEmpty(searchVO.getStartDate()) && CharSequenceUtil.isNotEmpty(searchVO.getEndDate()),
//                        File::getCreateTime, searchVO.getStartDate(), searchVO.getEndDate());
//        return this.page(PageUtil.initPage(pageVo), queryWrapper);
//    }
}