package com.wteam.framework.modules.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;

import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.security.token.Token;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.admin.entity.dos.Admin;

import com.wteam.framework.modules.member.entity.dos.Member;

import java.util.List;

/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
public interface AdminService extends IService<Admin> {

    Token login(String username, String password);

    String logo();

    void logout(UserEnums manager);

    void editPassword(String password, String newPassword);

    void saveAdminUser(Admin admin);

    void superSaveUser(Admin admin);

    ResultMessage addPhoneAccount(Member member);

    ResultMessage deleteMember(String id);


    Token AppLogin(String username, String password);

    void deleteCompletely(List<String> ids);


    Token phoneLogin(String mobile);


    ResultMessage appEditPassword(String moblie, String code, String password, String newPassword);
}