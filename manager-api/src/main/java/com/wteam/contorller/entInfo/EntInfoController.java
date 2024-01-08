package com.wteam.contorller.entInfo;


import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;




/**
 * 厂商、企业PC端
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-12
 */
@RestController
@RequestMapping("staff/entinfo")
@Api(tags = "厂商、企业PC端")
public class EntInfoController {

    @Autowired
    private EntInfoService entInfoService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<EntInfo>> page(EntInfo entity,
                                                                SearchVO searchVo,
                                                                PageVO page) {
        IPage<EntInfo> data = entInfoService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<EntInfo> get(@PathVariable("id") Long id) {
        EntInfo data = entInfoService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody EntInfo entity) {
        //效验数据
        return entInfoService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody EntInfo entity) {
        //效验数据
        return entInfoService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return entInfoService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}