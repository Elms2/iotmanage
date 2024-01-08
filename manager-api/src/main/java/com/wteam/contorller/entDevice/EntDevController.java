package com.wteam.contorller.entDevice;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.util.MapUtils;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.jetlinks.DeviceUtils;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.entity.dto.AdminDTO;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.deviceInstance.entity.dos.DevDeviceInstance;
import com.wteam.framework.modules.deviceInstance.entity.dto.DevDeviceInstanceDTO;
import com.wteam.framework.modules.deviceInstance.entity.vo.DeviceVo;
import com.wteam.framework.modules.deviceInstance.service.DevDeviceInstanceService;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.file.entity.vo.FileVo;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.userDevice.service.UserDevService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 企业设备中间表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("entdev")
@Api(tags = "企业设备中间表")
public class EntDevController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private UserDevService userDevService;


    @Autowired
    private EntDevService entDevService;

    @Autowired
    private BeanSearcher beanSearcher;

    @Autowired
    private DevDeviceInstanceService devInstance;

    /**
     * 企业设备
     *
     * @return
     */
    @PostMapping("sort")
    public ResultMessage sort(@RequestBody List<DevDeviceInstance> list) {
        //查出这个企业的设备
//        String enId = UserContext.getCurrentUser().getId();
//        List<EntDev> list = entDevService.query().eq("en_id", enId).list();

//        entDevService.industryDevice(map);
        return devInstance.updateBatchById(list) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


    @PostMapping(value = "/super/{page}/{size}")
    public ResultMessage devSearch(@PathVariable Long page, @PathVariable Integer size, @RequestBody DevDeviceInstanceDTO devDTO) {


        String enId = devDTO.getId();
        String factId = devDTO.getParentId();
        Map<String, Object> build = new HashMap<>(10);

        if (ObjectUtil.isNull(enId) && ObjectUtil.isNotNull(factId)) {
            build = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(DeviceVo::getParentId)
                    .sql("$1 = ? ", factId)
                    .limit(page, size)
                    .build();
        } else if (ObjectUtil.isNotNull(enId) && ObjectUtil.isNotNull(factId)) {
            build = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(DeviceVo::getEnId, DeviceVo::getParentId)
                    .sql(" $1 = ? and $2 = ? ", enId, factId)
                    .limit(page, size)
                    .build();
        } else {
            build = MapUtils.builder().limit(page, size).build();
        }

        Map<String, Object> map = PageUtil.convertToMap(devDTO, build);
        SearchResult<DeviceVo> search = beanSearcher.search(DeviceVo.class, map);

        return ResultUtil.data(search);

    }

    /**
     * 厂商添加设备
     *
     * @param map
     * @return
     */
    @PostMapping("create")
    public ResultMessage create(@RequestBody Map<String, Object> map) {

        String s = map.get("id").toString();
        DevDeviceInstance one = devInstance.query().eq("id", s).one();
        if (ObjectUtil.isNotNull(one)) {
            throw new ServiceException(ResultCode.DEVICE_EXISTS);
        }
        entDevService.industryDevice(map);
        return ResultUtil.success();
    }


    /**
     * 设备列表
     *
     * @return
     */
    @GetMapping({"/enterp/all"})
    public ResultMessage all() {

        IPage<DevDeviceInstance> all = userDevService.AdminAll();
        return ResultUtil.data(all);
    }


    /**
     * 设备列表
     *
     * @return
     */
    @GetMapping({"/enterp/search"})
    public ResultMessage all(@RequestParam String id) {

        IPage<DevDeviceInstance> all = userDevService.AdminAll(id);
        return ResultUtil.data(all);
    }


    /**
     * 删除设备
     *
     * @return
     */
    @PostMapping("/delete/{id}")
    public ResultMessage delete(@PathVariable String id) {
        AuthUser currentUser = UserContext.getCurrentUser();
        String enId = currentUser.getId();
        Admin one = adminService.query().eq("id", enId).one();
        if (ObjectUtil.isNull(one)) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        if (("0").equals(one.getPlatformFlag())) {
            return entDevService.remove(new QueryWrapper<EntDev>().eq("dev_id", id)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
        }

        return entDevService.remove(new QueryWrapper<EntDev>().eq("en_id", enId).eq("dev_id", id)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }





    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<EntDev>> page(EntDev entity,
                                             SearchVO searchVo,
                                             PageVO page) {
        IPage<EntDev> data = entDevService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<EntDev> get(@PathVariable("id") Long id) {
        EntDev data = entDevService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody EntDev entity) {
        //效验数据
        return entDevService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody EntDev entity) {
        //效验数据
        return entDevService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return entDevService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}