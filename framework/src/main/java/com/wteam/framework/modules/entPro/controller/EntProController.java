package com.wteam.framework.modules.entPro.controller;

import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
 import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.entPro.service.EntProService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;




/**
 * 企业产品中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-18
 */
@RestController
@RequestMapping("staff/entpro")
@Api(tags = "企业产品中间表")
public class EntProController {

    @Autowired
    private EntProService entProService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<EntPro>> page(EntPro entity,
                                                                SearchVO searchVo,
                                                                PageVO page) {
        IPage<EntPro> data = entProService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<EntPro> get(@PathVariable("id") Long id) {
        EntPro data = entProService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody EntPro entity) {
        //效验数据
        return entProService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody EntPro entity) {
        //效验数据
        return entProService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return entProService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}