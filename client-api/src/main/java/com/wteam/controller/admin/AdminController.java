package com.wteam.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;

import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.security.token.Token;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.mapper.MemberMapper;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Slf4j
@RestController
@RequestMapping("/app/admin")
@Api(tags = "app端")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private MemberMapper memberMapper;




    @Autowired
    private EntInfoService entInfoService;


    @Autowired
    private EntDevService entDevService;
    /**
     * app中登录(包括admin和member)
     *
     * @param admin
     * @return
     */
    @PostMapping(value = "/login")
    //@ApiOperation(value = "登录管理员")
    public ResultMessage<Token> AppLogin(@RequestBody Admin admin) {
        return ResultUtil.data(adminService.AppLogin(admin.getUsername(), admin.getPassword()));
    }


    @PostMapping(value = "/sms/login/{mobile}/{code}")
    //@ApiOperation(value = "登录管理员")
    public ResultMessage<Token> phone(@PathVariable String mobile, @PathVariable String code) {
        if (smsUtil.verifyCode(mobile, code)) {
            return ResultUtil.data(adminService.phoneLogin(mobile));
        } else {
            throw new ServiceException(ResultCode.VERIFICATION_SMS_CHECKED_ERROR);
        }
    }

    /**
     * @return * @return ResultMessage
     * @method
     * @description 退出登录
     * @date: 2022/9/12 12:24
     * @author: lsllsl
     */
    @GetMapping("/logout")
    @Transactional
    public ResultMessage<Object> logout() {
        /**
         * 获取现在登录对象的角色
         * 如果是admin,删除的时候redis上面的应该是userEnums.MANAGER
         * 如果是menber,删除的时候redis上面的应该是userEnums.MEMBER
         */
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser.getRole() == UserEnums.MANAGER) {
            this.adminService.logout(UserEnums.MANAGER);
        } else {
            this.adminService.logout(UserEnums.MEMBER);
        }
        return ResultUtil.success();
    }

    //    /**
//     * 修改密码
//     *
//     * @param password
//     * @param newPassword
//     * @return
//     */
//    @PutMapping(value = "/editPassword")
//    //@ApiOperation(value = "修改密码")
//    public ResultMessage<Object> editPassword(@RequestParam String password,@RequestParam  String newPassword) {
//        this.adminService.editPassword(password, newPassword);
//        return ResultUtil.success(ResultCode.USER_EDIT_SUCCESS);
//    }
//

    @GetMapping("/getUserInfo")
    public ResultMessage getUserInfo() {
        AuthUser currentUser = UserContext.getCurrentUser();
        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        if (null != admin){
           return ResultUtil.data(admin);
       }else{

            Member member = (Member)((QueryChainWrapper)this.memberService.query().eq("id", currentUser.getId())).one();
            String enId = member.getEnId();
            EntInfo info = (EntInfo)((QueryChainWrapper)this.entInfoService.query().eq("en_id", enId)).one();
            member.setEntInfo(info);
            Long count = ((QueryChainWrapper)this.entDevService.query().eq("en_id", enId)).count();
            member.setCnt(count.intValue());
            return ResultUtil.data(member);

        }
    }



    
//    /**
//     * @ApiParam("用户唯一id标识")
//     */
//    @PutMapping(value = "/enable/{userId}")
//    public ResultMessage<Object> disable(@PathVariable String userId, Boolean status) {
//        Admin user = adminService.getById(userId);
//        if (user == null) {
//            throw new ServiceException(ResultCode.USER_NOT_EXIST);
//        }
//        user.setStatus(status);
//        adminService.updateById(user);
//        return ResultUtil.success();
//    }
//
    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<Admin>> page(Admin entity,
                                            SearchVO searchVo,
                                            PageVO page) {
        IPage<Admin> data = adminService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        List<Admin> records = data.getRecords();
        records.stream().filter(x -> {
            return x.getParentId() != null;
        }).forEach(k -> {
            EntInfo info = entInfoService.query().eq("en_id", k.getId()).one();
            k.setEntInfo(info);
        });

        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<Admin> get(@PathVariable("id") Long id) {
        Admin data = adminService.getById(id);
        EntInfo info = entInfoService.query().eq("en_id", data.getId()).one();
        data.setEntInfo(info);
        return ResultUtil.data(data);
    }
//
//    @PostMapping("/register")
//    public ResultMessage<Object> register(@RequestBody @Valid Admin adminUser) {
//        int rolesMaxSize = 10;
//        try {
//            if (adminUser.getUserRoles() != null && adminUser.getUserRoles().size() >= rolesMaxSize) {
//                throw new ServiceException(ResultCode.PERMISSION_BEYOND_TEN);
//            }
//            adminService.saveAdminUser(adminUser,adminUser.getUserRoles());
//        } catch (Exception e) {
//            log.error("添加用户错误", e);
//        }
//        return ResultUtil.success();
//    }
//
//    @PutMapping
//    @ApiOperation("修改")
//    public ResultMessage update(@RequestBody Admin entity) {
//        //效验数据
//        return adminService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
//    }
//
//    @DeleteMapping
//    @ApiOperation("删除")
//    public ResultMessage delete(@RequestBody List<String> ids) {
//        //效验数据
//        return adminService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
//    }
    @GetMapping("/getEnId")
    public ResultMessage getEnId (){
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser.getRole() == UserEnums.MANAGER){
            return  ResultUtil.data(currentUser.getId());
        }else {
            return   ResultUtil.data(memberService.query().eq("id",currentUser.getId()).one().getEnId());
        }

    }

    /**
     * 企业管理员还是员工,再手机号码然后登录
     */
    /**
     * 修改密码
     *
     * @param password
     * @param newPassword
     * @return
     */
    @GetMapping(value = "/appEditPassword")
    //@ApiOperation(value = "修改密码")
    public ResultMessage<Object> appEditPassword(@RequestParam String phone,
                                                 @RequestParam String code,
                                                 @RequestParam String password,
                                                 @RequestParam  String newPassword) {
        /**
         * 0.发送短信验证码
         /common/sms/{mobile}
         * 1.校对验证码
         * 2.查看时admin还是member表的用户
         * 3.查看手机号码对应的手机号,然后校对旧的密码
         * 4.对了再更新密码
         */
        return  this.adminService.appEditPassword(phone,code,password, newPassword);
    }

    @PostMapping("/updateUserMessage")
    public ResultMessage updateMessage (@RequestBody Member member){
        AuthUser currentUser = UserContext.getCurrentUser();
        member.setId(currentUser.getId());
//        if (currentUser.getRole() == UserEnums.MANAGER){
//            //是admin表
//            if (null != admin.getUsername()){
//                Admin username = adminService.query().eq("username", admin.getUsername()).one();
//                if (null != username){
//                    return ResultUtil.error(ResultCode.USERNAME_USED);
//                }else{
//                    adminService.updateById(admin);
//                    return  ResultUtil.data("修改用户信息成功");
//                }
//            }else{
//                adminService.updateById(admin);
//                return  ResultUtil.data("修改用户信息成功");
//            }
//
//
//        }else {
            //是member表
            if (null != member.getUsername()){
                Member one = memberService.query().eq("username", member.getUsername()).one();
                if (null != one){
                    return ResultUtil.error(ResultCode.USERNAME_USED);
                }else{
                    memberService.updateById(member);
                    return  ResultUtil.data("修改用户信息成功");
                }
            }else{
                memberService.updateById(member);
                return  ResultUtil.data("修改用户信息成功");
            }
//        }

    }
    //企业管理员添加企业操作员和企业管理员
    @PostMapping("/addPhoneAccount")
    public ResultMessage<Object> addPhoneAccount(@RequestBody Member member) {
        return adminService.addPhoneAccount(member);
    }

    @GetMapping("/queryWorker")
    public ResultMessage<Object> queryWorker(@RequestParam("pageNumber")Integer pageNumber,@RequestParam("pageSize")Integer pageSize) {
        AuthUser currentUser = UserContext.getCurrentUser();
//        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        QueryWrapper queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("en_id",currentUser.getId());
        IPage<Member> page=new Page<>(pageNumber,pageSize);
        IPage selectPage = memberMapper.selectPage(page, queryWrapper);
        if (null != selectPage){
            return  ResultUtil.data(selectPage);
        }else{
            return ResultUtil.error(ResultCode.ENT_NULL_WORKER);
        }
    }

    //企业管理员删除员工
    @GetMapping("/deteleMember/{id}")
    public ResultMessage<Object> deteleMember(@PathVariable("id")String id) {
        return adminService.deleteMember(id);
    }
    //企业管理员修改员工信息-------你问问是要员工所有的信息都可以修改还是?
    @PostMapping("/updateMember")
    public ResultMessage<Object> updateMember(@RequestBody Member member) {
        if (null != member.getPassword()){
            String newEncryptPass = new BCryptPasswordEncoder().encode(member.getPassword());
            member.setPassword(newEncryptPass);
        }
        boolean b = memberService.updateById(member);
        if (b){
            return ResultUtil.success(ResultCode.SUCCESS);
        }else{
            return ResultUtil.error(ResultCode.ERROR);
        }

    }
}