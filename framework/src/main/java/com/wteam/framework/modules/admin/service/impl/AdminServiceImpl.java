package com.wteam.framework.modules.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wteam.framework.common.cache.Cache;
import com.wteam.framework.common.cache.CachePrefix;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;

import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.security.token.Token;
import com.wteam.framework.common.security.token.manager.ManagerTokenGenerate;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.mapper.AdminMapper;

import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.member.mapper.MemberMapper;
import com.wteam.framework.modules.member.token.MemberTokenGenerate;
import com.wteam.framework.modules.roleMenu.service.RoleService;
import com.wteam.framework.modules.system.SmsUtil;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.service.SettingService;
import com.wteam.framework.modules.userRole.entity.dos.UserRole;
import com.wteam.framework.modules.userRole.service.UserRoleService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.wteam.framework.modules.admin.entity.enums.userEnum.*;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {


    @Autowired
    private ManagerTokenGenerate managerTokenGenerate;

    @Autowired
    private MemberTokenGenerate memberTokenGenerate;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private Cache cache;
    @Autowired
    private MemberService memberService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private EntInfoService entInfoService;

    @Autowired
    private SmsUtil smsUtil;


    @Override
    public Token login(String username, String password) {
        Admin adminUser = this.query().eq("username", username).one();

        if (adminUser == null || !adminUser.getStatus()) {
            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        if (!new BCryptPasswordEncoder().matches(password, adminUser.getPassword())) {
            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
        }
        try {
            return managerTokenGenerate.createToken(adminUser, false);
        } catch (Exception e) {
            log.error("管理员登录错误", e);
        }
        return null;
    }

    @Override
    public String logo() {
        Setting systeminfo_setting = settingService.get("SYSTEMINFO_SETTING");
        return systeminfo_setting.getSettingValue();

    }

    @Override
    public void logout(UserEnums userEnums) {
        String currentUserToken = UserContext.getCurrentUserToken();
        if (CharSequenceUtil.isNotEmpty(currentUserToken)) {
            cache.remove(CachePrefix.ACCESS_TOKEN.getPrefix(userEnums) + currentUserToken);
        }
    }

    @Override
    public void editPassword(String password, String newPassword) {
        AuthUser tokenUser = UserContext.getCurrentUser();
        Admin user = this.getById(tokenUser.getId());
        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new ServiceException(ResultCode.USER_OLD_PASSWORD_ERROR);
        }
        String newEncryptPass = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(newEncryptPass);
        this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAdminUser(Admin admin) {


        Admin username = this.query().eq("username", admin.getUsername()).one();
        if (ObjectUtil.isNotNull(username)) {
            throw new ServiceException(ResultCode.USERNAME_USED);
        }
        Admin dbUser = new Admin();
        AuthUser tokenUser = UserContext.getCurrentUser();
        Admin user = this.getById(tokenUser.getId());
        BeanUtil.copyProperties(admin, dbUser);
        dbUser.setParentId(user.getId());
        dbUser.setPassword(new BCryptPasswordEncoder().encode(dbUser.getPassword()));
        this.save(dbUser);
        dbUser = this.query().eq("username", dbUser.getUsername()).one();

        EntInfo entInfo = EntInfo.builder()
                .enId(dbUser.getId())
                .enLogo(admin.getLogo())
                //enterpriseName
                .enName(admin.getEnterpriseName())
                .projectName(admin.getProjectName())
                .build();

        entInfoService.save(entInfo);
    }

    @Override
    public void superSaveUser(Admin admin) {
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        String role = admin.getUserRole();
        this.save(admin);
        admin = this.query().eq("username", admin.getUsername()).one();


        EntInfo entInfo = EntInfo.builder()
                .enId(admin.getId())
                .enLogo(admin.getLogo())
                .enName(admin.getNickName())
                .build();

        entInfoService.save(entInfo);

        updateRole(admin.getId(), role);
    }

    /**
     * 更新用户角色
     *
     * @param userId 用户id
     * @param roles  角色id集合
     */
    private void updateRole(String userId, String roles) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        userRoleService.remove(queryWrapper);
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(new UserRole(userId, roles));
        userRoleService.updateUserRole(userId, userRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage addPhoneAccount(Member entity) {
        //企业为手机端添加一个企业员工账号
        /**
         * 1.首先在member添加一条数据
         * 2.手机端好像也要权限,不是写死的,所以顺便给用户-角色表添加一条数据
         */
        AuthUser currentUser = UserContext.getCurrentUser();
       if (null ==memberService.query().eq("phone",entity.getPhone()).one()) {

           if (currentUser.getRole() != UserEnums.MANAGER && !currentUser.getIsSuper()) {
               //超级管理员
               entity.setEnId( currentUser.getId() );
           }
           String newEncryptPass = new BCryptPasswordEncoder().encode(entity.getPassword());
           entity.setPassword(newEncryptPass);
           boolean save1 = memberService.save(entity);
           UserRole userRole = new UserRole();
           userRole.setRoleId(entity.getRoleId());
           userRole.setUserId(entity.getId());
           boolean save2 = userRoleService.save(userRole);
           return save1 && save2 ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
       }else{
           throw new ServiceException(ResultCode.PHONE_USED);
//           return ResultUtil.data(ResultCode.PHONE_USED);
        }
    }

    @Override
    @Transactional
    public ResultMessage deleteMember(String id) {
        /**
         * 企业管理员删除企业员工账号
         * 1.删除member表
         * 2.删除中间表
         */
        boolean remove1 = memberService.removeById(id);
        List<UserRole> userRoleList = userRoleService.query().eq("user_id", id).list();
        for (UserRole userRole : userRoleList) {
            userRoleService.removeById(userRole);
        }
        return ResultUtil.success();
    }

    @Override
    public Token AppLogin(String username, String password) {
        /**
         * app中admin和member的登录
         * 1.查询admin和member表中是否有用户名是这个username的用户
         * 2.判断账号的状态是否可用
         * 3.判断用户的密码是否正确
         * 4.如果以上条件去全部满足,就
         */
        Admin adminUser = this.query().eq("username", username).one();
        Member memberUser = memberService.query().eq("username", username).one();

        if (adminUser == null || !adminUser.getStatus()) {
            if (memberUser == null || memberUser.getDisabled()) {
                throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
            }
        }
        if (adminUser != null) {
            if (!new BCryptPasswordEncoder().matches(password, adminUser.getPassword())) {
                throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
            }
        } else {
            if (!new BCryptPasswordEncoder().matches(password, memberUser.getPassword())) {
                throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
            }
        }

        try {
            if (adminUser != null) {
                return managerTokenGenerate.createToken(adminUser, true);
            } else {
                return memberTokenGenerate.createToken(memberUser, true);
            }
        } catch (Exception e) {
            log.error("登录错误", e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCompletely(List<String> ids) {
        //彻底删除超级管理员
        this.removeByIds(ids);

        //删除管理员角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        userRoleService.remove(queryWrapper);
    }


    @Override
    public Token phoneLogin(String mobile) {
        Member memberUser = memberService.query().eq("phone", mobile).one();
        //有必要可以直接帮他注册，我懒得做

        if (ObjectUtil.isNull(memberUser)) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }

        return memberTokenGenerate.createToken(memberUser, false);

    }

    @Override
    public ResultMessage appEditPassword(String moblie, String code, String password, String newPassword) {
        /**
         * 0.发送短信验证码
         /common/sms/{mobile}
         * 1.校对验证码
         * 2.查看时admin还是member表的用户
         * 3.查看手机号码对应的手机号,然后校对旧的密码
         * 4.对了再更新密码
         */
        if (!new BCryptPasswordEncoder().matches(password, newPassword)) {
            if (smsUtil.verifyCode(moblie, code)) {
                Admin admin = this.query().eq("phone", moblie).one();
                if (null == admin) {
                    Member member = memberService.query().eq("phone", moblie).one();
                    if (null != member) {
                        if (!new BCryptPasswordEncoder().matches(password, member.getPassword())) {
                            throw new ServiceException(ResultCode.USER_ORIG_PASSWORD_ERROR);
                        } else {
                            String newEncryptPass = new BCryptPasswordEncoder().encode(newPassword);
                            member.setPassword(newEncryptPass);
                            memberService.updateById(member);
                            return ResultUtil.data("密码修改成功");
                        }

                    } else {
                        return ResultUtil.error(ResultCode.PHONENUMBER_ERROR);
                    }
                } else {
                    //admin登录
                    if (!new BCryptPasswordEncoder().matches(password, admin.getPassword())) {
                        throw new ServiceException(ResultCode.USER_ORIG_PASSWORD_ERROR);
                    } else {
                        String newEncryptPass = new BCryptPasswordEncoder().encode(newPassword);
                        admin.setPassword(newEncryptPass);
                        this.updateById(admin);
                        return ResultUtil.data("密码修改成功");
                    }
                }

            } else {
                return ResultUtil.error(ResultCode.CODE_ERROR);
            }
        } else {
            return ResultUtil.error(ResultCode.OLD_NEWPASSWORD_SAME);
        }
    }
}