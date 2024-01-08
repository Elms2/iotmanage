package com.wteam.framework.modules.deviceInstance.controller;

import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;

import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;




/**
 * 设备信息表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-25
 */
@RestController
@RequestMapping("staff/devdeviceinstance")
@Api(tags = "设备信息表")
public class DevDeviceInstanceController {

    @Autowired
    private DevDeviceInstanceService devDeviceInstanceService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<DevDeviceInstance>> page(DevDeviceInstance entity,
                                                        SearchVO searchVo,
                                                        PageVO page) {
        IPage<DevDeviceInstance> data = devDeviceInstanceService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<DevDeviceInstance> get(@PathVariable("id") Long id) {
        DevDeviceInstance data = devDeviceInstanceService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody DevDeviceInstance entity) {
        //效验数据
        return devDeviceInstanceService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody DevDeviceInstance entity) {
        //效验数据
        return devDeviceInstanceService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return devDeviceInstanceService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}