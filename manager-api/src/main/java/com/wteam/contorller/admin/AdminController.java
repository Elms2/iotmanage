package com.wteam.contorller.admin;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejlchina.searcher.BeanSearcher;
import com.wteam.framework.common.enums.ResultUtil;

import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.security.token.Token;
import com.wteam.framework.common.utils.BeanUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.admin.entity.vo.AdminVo;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.mapper.MemberMapper;
import com.wteam.framework.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@Slf4j
@RestController
@RequestMapping("/staff/admin")
@Api(tags = "厂商、企业PC端")
public class AdminController {
    @Autowired
    private EntInfoService entInfoService;

    @Autowired
    private AdminService adminService;
    @Autowired
    private MemberMapper memberMapper;


    @Autowired
    private EntDevService entDevService;



    @PostMapping(value = "/searchList")
    public ResultMessage searchList() {
        List<AdminVo> adminVoList = new ArrayList<>();
        List<Admin> list = adminService.query().isNull("parent_id").eq("is_super",0).list();
        for (Admin one : list) {
            AdminVo adminVo = new AdminVo();
            BeanUtil.copyProperties(one, adminVo);
            List<Admin> enterp = adminService.query().eq("parent_id", one.getId()).list();
            adminVo.setEnterprises(enterp);
            adminVoList.add(adminVo);
        }
        return ResultUtil.data(adminVoList);

    }


    @GetMapping("super/page")
    @ApiOperation("分页")
    public ResultMessage<IPage<Admin>> superPage(Admin entity,
                                                 SearchVO searchVo,
                                                 PageVO page) {

        IPage<Admin> data = adminService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        data.getRecords().forEach(x->{
            String enId = x.getId();
            EntInfo info = entInfoService.query().eq("en_id", enId).one();
//            x.setEntInfo(info);
            x.setLogo(info.getEnLogo());
            x.setEnterpriseName(info.getEnName());
            x.setProjectName(info.getProjectName());

        });
        return ResultUtil.data(data);

    }

    @GetMapping(value = "/logo")
    public ResultMessage<String> logo(
    ) {
        return ResultUtil.data(adminService.logo());
    }

    @PostMapping(value = "/login")
    //@ApiOperation(value = "登录管理员")
    public ResultMessage<Token> login(@RequestBody Admin admin
    ) {

        return ResultUtil.data(adminService.login(admin.getUsername(), admin.getPassword()));

    }

    /**
     * @return * @return ResultMessage
     * @method
     * @description 退出登录
     * @date: 2022/9/12 12:24
     * @author: lsllsl
     */
    @PostMapping("/logout")
    public ResultMessage<Object> logout() {
        this.adminService.logout(UserEnums.MANAGER);
        return ResultUtil.success();
    }

    /**
     * 修改密码
     *
     * @param password
     * @param newPassword
     * @return
     */
    @PutMapping(value = "/editPassword")
    //@ApiOperation(value = "修改密码")
    public ResultMessage<Object> editPassword(@RequestParam String password, @RequestParam String newPassword) {
        this.adminService.editPassword(password, newPassword);
        return ResultUtil.success(ResultCode.USER_EDIT_SUCCESS);
    }

    @PostMapping("/UserCenter")
    public ResultMessage UserCenter() {
        Map<String, Object> map = new HashMap<>(3);

        AuthUser currentUser = UserContext.getCurrentUser();
        EntInfo entInfo = entInfoService.query().eq("en_id", currentUser.getId()).one();
        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        admin.setLogo(entInfo.getEnLogo());
        if(currentUser.getIsSuper()){
            map.put("admin", admin);
            return ResultUtil.data(map);
        }
        if (admin.getPlatformFlag()==0||admin.getPlatformFlag().equals(0)) {
            //企业数量
            long EnCount = adminService.count(new QueryWrapper<Admin>().eq("parent_id", currentUser.getId()));
            map.put("EnCount", EnCount);
        }

        //设备数量
        long DevCount = entDevService.count(new QueryWrapper<EntDev>().eq("en_id", currentUser.getId()));

        map.put("DevCount", DevCount);
        map.put("admin", admin);

        return ResultUtil.data(map);
    }


    @GetMapping("/getUserInfo")
    public ResultMessage getUserInfo() {
        AuthUser currentUser = UserContext.getCurrentUser();
        Admin admin = adminService.query().eq("id", currentUser.getId()).one();

        return ResultUtil.data(admin);
    }


    /**
     * @ApiParam("用户唯一id标识")
     */
    @PutMapping(value = "/enable/{userId}")
    public ResultMessage<Object> disable(@PathVariable String userId, Boolean status) {
        Admin user = adminService.getById(userId);
        if (user == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        user.setStatus(status);
        adminService.updateById(user);
        return ResultUtil.success();
    }

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<Admin>> page(Admin entity,
                                            SearchVO searchVo,
                                            PageVO page) {
        String id = UserContext.getCurrentUser().getId();
        entity.setParentId(id);
        IPage<Admin> data = adminService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        data.getRecords().forEach(x->{
            String enId = x.getId();
            EntInfo info = entInfoService.query().eq("en_id", enId).one();
//            x.setEntInfo(info);
            x.setLogo(info.getEnLogo());
            x.setEnterpriseName(info.getEnName());
            x.setProjectName(info.getProjectName());

        });
        return ResultUtil.data(data);

    }



    @GetMapping("ent")
    @ApiOperation("分页")
    public ResultMessage<IPage<Admin>> ent(Admin entity,
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


    @GetMapping("GetEnt")
    @ApiOperation("分页")
    public ResultMessage<IPage<Admin>> GetEnt(Admin entity,
                                              SearchVO searchVo,
                                              PageVO page) {
        String id = UserContext.getCurrentUser().getId();
        entity.setParentId(id);
        IPage<Admin> data = adminService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));

        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<Admin> get(@PathVariable("id") Long id) {
        Admin data = adminService.getById(id);

        return ResultUtil.data(data);
    }

    /**
     * 添加企业账号
     * @param adminUser
     * @return
     */
    @PostMapping
    public ResultMessage<Object> register(@RequestBody @Valid Admin adminUser) {
        try {
            adminService.saveAdminUser(adminUser);
        } catch (Exception e) {
            log.error("添加用户错误", e);
        }
        return ResultUtil.success();
    }

    @PostMapping(value = "/super")
    public ResultMessage<Object> superRegister(@RequestBody @Valid Admin adminUser) {
        try {
            adminService.saveAdminUser(adminUser);
        } catch (Exception e) {
            log.error("添加用户错误", e);
        }
        return ResultUtil.success();
    }

    @PutMapping
    @ApiOperation("更新信息")
    public ResultMessage update(@RequestBody Admin entity) {
        //效验数据
//        String logo = entity.getLogo();
//        if (ObjectUtil.isNotNull(logo)) {
//            //后面不加update会死
//            entInfoService.update().eq("en_id", entity.getId()).set("en_logo", logo).update();
//        }
        String enId = entity.getId();
        EntInfo info = entInfoService.query().eq("en_id", enId).one();
        if (ObjectUtil.isNotNull(info)){
            info.setEnName(entity.getEnterpriseName());
            info.setProjectName(entity.getProjectName());
            info.setEnLogo(entity.getLogo()
            );
        }
        return adminService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage<Object> delAllByIds(@RequestBody Map<String, List<String>> map) {
        adminService.deleteCompletely(map.get("ids"));
        return ResultUtil.success();
    }

    //企业管理员添加企业操作员和企业管理员
    @PostMapping("/addPhoneAccount")
    public ResultMessage<Object> addPhoneAccount(@RequestBody Member member) {
        return adminService.addPhoneAccount(member);
    }

    //企业管理员删除员工
    @GetMapping("/deteleMember/{id}")
    public ResultMessage<Object> deteleMember(@PathVariable("id") String id) {
        return adminService.deleteMember(id);
    }


    @GetMapping("/queryWorker/{role}/{enId}")
    public ResultMessage<Object> queryWorker(@PathVariable String role,
                                             @PathVariable String enId,
                                             @RequestParam("pageNumber") Integer pageNumber,
                                             @RequestParam("pageSize") Integer pageSize) {
//        AuthUser currentUser = UserContext.getCurrentUser();
//        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        QueryWrapper queryWrapper = new QueryWrapper<Member>();
        queryWrapper.eq("role", role);
        queryWrapper.eq("en_id", enId);
        IPage<Member> page = new Page<>(pageNumber, pageSize);
        EntInfo info = entInfoService.query().eq("en_id", enId).one();


        IPage<Member> selectPage = memberMapper.selectPage(page, queryWrapper);
        selectPage.getRecords().forEach(x -> {
            x.setEntInfo(info);
        });
        if (null != selectPage) {
            return ResultUtil.data(selectPage);
        } else {
            return ResultUtil.error(ResultCode.ENT_NULL_WORKER);
        }
    }


}