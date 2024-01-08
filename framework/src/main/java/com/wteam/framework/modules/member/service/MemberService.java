package com.wteam.framework.modules.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.member.entity.dos.Member;

/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
public interface MemberService extends IService<Member> {

    ResultMessage saveMemberUser(Member member);//, String roleId
}