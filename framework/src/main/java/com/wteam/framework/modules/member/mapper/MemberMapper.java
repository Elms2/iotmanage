package com.wteam.framework.modules.member.mapper;
import com.wteam.framework.modules.member.entity.dos.Member;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;


/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
	
}