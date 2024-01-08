package com.wteam.contorller.userDevice;


import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.userDevice.entity.dos.UserDev;
import com.wteam.framework.modules.userDevice.service.UserDevService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;




/**
 * 员工设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("staff/userdev")
@Api(tags = "员工设备中间表")
public class UserDevController {

    @Autowired
    private UserDevService userDevService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<UserDev>> page(UserDev entity,
                                              SearchVO searchVo,
                                              PageVO page) {
        IPage<UserDev> data = userDevService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<UserDev> get(@PathVariable("id") Long id) {
        UserDev data = userDevService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody UserDev entity) {
        //效验数据
        return userDevService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody UserDev entity) {
        //效验数据
        return userDevService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return userDevService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}