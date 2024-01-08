package com.wteam.framework.modules.entDevice.mapper;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 企业设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Mapper
public interface EntDevMapper extends BaseMapper<EntDev> {
	
}