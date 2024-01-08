package com.wteam.contorller.userRole;


import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.userRole.entity.dos.UserRole;
import com.wteam.framework.modules.userRole.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;
import java.util.List;


/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("staff/userrole")
@Api(tags = "")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<UserRole>> page(UserRole entity,
                                                                SearchVO searchVo,
                                                                PageVO page) {
        IPage<UserRole> data = userRoleService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));

        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<UserRole> get(@PathVariable("id") Long id) {
        UserRole data = userRoleService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody UserRole entity) {
        //效验数据
        return userRoleService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody UserRole entity) {
        //效验数据
        return userRoleService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return userRoleService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}