package com.wteam.contorller.roleMenu;


import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.role.service.RoleMenuService;
import com.wteam.framework.modules.roleMenu.entity.dos.RoleMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Arrays;




/**
 * 
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("staff/rolemenu")
@Api(tags = "")
public class RoleMenuController {

    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<RoleMenu>> page(RoleMenu entity,
                                                                SearchVO searchVo,
                                                                PageVO page) {
        IPage<RoleMenu> data = roleMenuService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<RoleMenu> get(@PathVariable("id") Long id) {
        RoleMenu data = roleMenuService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody RoleMenu entity) {
        //效验数据
        return roleMenuService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody RoleMenu entity) {
        //效验数据
        return roleMenuService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return roleMenuService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}