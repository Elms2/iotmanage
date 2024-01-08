package com.wteam.contorller.member;


import cn.hutool.core.util.ObjectUtil;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.util.MapUtils;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.admin.entity.dto.AdminDTO;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.file.entity.vo.FileVo;
import com.wteam.framework.modules.file.service.FileService;
import com.wteam.framework.modules.member.entity.dto.MemberDTO;
import com.wteam.framework.modules.member.entity.vo.MemberVo;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.member.entity.dos.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("/staff/member")
@Api(tags = "用户表")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BeanSearcher beanSearcher;


    @Autowired
    private FileService fileService;


    @PostMapping(value = "/super/{page}/{size}")
    public ResultMessage memberSearch(@PathVariable Long page,@PathVariable Integer size,@RequestBody MemberDTO memberDTO) {


        Map<String, Object> build = new HashMap<>(10);


        if (ObjectUtil.isNull(memberDTO.getEnId()) && ObjectUtil.isNotNull(memberDTO.getParentId())) {
            build = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(MemberVo::getParentId)
                    .sql("$1 = ? ", memberDTO.getParentId())
                    .limit(page,size)
                    .build();
        } else if (ObjectUtil.isNotNull(memberDTO.getEnId()) && ObjectUtil.isNotNull(memberDTO.getParentId())) {
            build = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(MemberVo::getEnId, MemberVo::getParentId)
                    .sql(" $1 = ? and $2 = ? ", memberDTO.getEnId(), memberDTO.getParentId())
                    .limit(page,size)
                    .build();
        } else {
            build = MapUtils.builder().limit(page, size).build();
        }


        Map<String, Object> map = PageUtil.convertToMap(memberDTO, build);

        SearchResult<MemberVo> search = beanSearcher.search(MemberVo.class, map);

        return ResultUtil.data(search);

    }

    @PostMapping("saveContent")
    public ResultMessage<Object> upload(@RequestBody File file) {

        AuthUser currentUser = UserContext.getCurrentUser();
        Member member = memberService.query().eq("id", currentUser.getId()).one();
        file.setEnId(member.getEnId());
        file.setState("2");
        return fileService.save(file) ? ResultUtil.success() : ResultUtil.error(ResultCode.PARAMS_ERROR);

    }



    @GetMapping("page")
    @ApiOperation("分页/员工列表")
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

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody Member entity) {
        //效验数据
        return memberService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping("/update")
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


    @GetMapping("/delete")
    public ResultMessage deleteone(){
        memberService.removeById(1);
        return null;
    }
}