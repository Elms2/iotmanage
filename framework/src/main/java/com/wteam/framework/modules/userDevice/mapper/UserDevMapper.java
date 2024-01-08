package com.wteam.framework.modules.userDevice.mapper;
import com.wteam.framework.modules.userDevice.entity.dos.UserDev;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 员工设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Mapper
public interface UserDevMapper extends BaseMapper<UserDev> {
	
}