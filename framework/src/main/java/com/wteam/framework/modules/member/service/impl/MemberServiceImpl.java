package com.wteam.framework.modules.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.mapper.MemberMapper;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.userRole.entity.dos.UserRole;
import com.wteam.framework.modules.userRole.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage saveMemberUser(Member member) {//, String roleId
        /**
         * 添加用户,并且给用户分配角色
         */

        Member phone = query().eq("phone", member.getPhone()).one();
        Member username = query().eq("username", member.getUsername()).one();
        if (phone != null || username != null) {
            throw new ServiceException(ResultCode.USER_EXIST);
        }
        Member dbUser = new Member();
        BeanUtil.copyProperties(member, dbUser);
        dbUser.setPassword(new BCryptPasswordEncoder().encode(dbUser.getPassword()));
        this.save(dbUser);
//        UserRole userRole = new UserRole();
//        userRole.setUserId(dbUser.getId());
//        userRole.setRoleId(dbUser.getRoleId());
//        userRoleService.save(userRole);
        return ResultUtil.success();
    }
}