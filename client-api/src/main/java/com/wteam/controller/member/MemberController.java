package com.wteam.controller.member;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;


/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("/app/member")
@Api(tags = "用户表")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SmsUtil smsUtil;

    @PostMapping("/register")
    public ResultMessage register(@RequestBody Member member) {
        try {
//            if (member.getRoleId() == null) {
//                throw new ServiceException(ResultCode.PERMISSION_NOT_ROLE);
//            }
            memberService.saveMemberUser(member);//.member.getRoleId()
        } catch (Exception e) {
            log.error("添加用户错误", e);
        }
        return ResultUtil.success();
    }


    @PostMapping("phone/register/{code}")
    public ResultMessage phoneRegister(@RequestBody Member member, @PathVariable String code) {

        if (smsUtil.verifyCode(member.getPhone(), code)) {
            return memberService.saveMemberUser(member);
        }
        return ResultUtil.error(ResultCode.USER_EXIST);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<Member>> page(Member entity,
                                             SearchVO searchVo,
                                             PageVO page) {
        IPage<Member> data = memberService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<Member> get(@PathVariable("id") Long id) {
        Member data = memberService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody Member entity) {
        //效验数据
        return memberService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody Member entity) {
        //效验数据
        return memberService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return memberService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}