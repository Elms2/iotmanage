package com.wteam.framework.modules.entPro.mapper;

import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 企业产品中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-18
 */
@Mapper
public interface EntProMapper extends BaseMapper<EntPro> {
	
}