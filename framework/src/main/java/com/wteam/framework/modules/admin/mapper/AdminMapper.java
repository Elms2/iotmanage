package com.wteam.framework.modules.admin.mapper;

import com.wteam.framework.modules.admin.entity.dos.Admin;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2022-11-10
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
	
}