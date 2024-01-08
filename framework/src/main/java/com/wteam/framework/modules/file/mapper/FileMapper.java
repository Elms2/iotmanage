package com.wteam.framework.modules.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import com.wteam.framework.modules.file.entity.dos.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件管理数据处理层
 *
 * @author Chopper
 * @since 2021-02-22 17:20
 */
 @Mapper
public interface FileMapper extends BaseMapper<File> {

}