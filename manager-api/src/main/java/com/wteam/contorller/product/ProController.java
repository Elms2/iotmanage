package com.wteam.contorller.product;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.util.MapUtils;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.jetlinks.ProductsUtils;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.entity.dto.AdminDTO;
import com.wteam.framework.modules.entDevice.entity.dos.EntDev;
import com.wteam.framework.modules.entPro.entity.dos.EntPro;
import com.wteam.framework.modules.entPro.service.EntProService;
import com.wteam.framework.modules.file.entity.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户表
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-01-11
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProController {

    @Autowired
    private EntProService entProService;

    @Autowired
    private ProductsUtils productsUtils;



    /**
     * 超级管理员查找产品
     *
     * @param
     * @return
     */
    @PostMapping("super/search")
    public JSONObject proSearch(@RequestBody AdminDTO adminDTO) {
//
        String factId = adminDTO.getFactId();
        return productsUtils.searchProduct(factId);
    }

    /**
     * 厂商添加产品
     *
     * @param map
     * @return
     */
    @PostMapping
    public ResultMessage create(@RequestBody Map<String, Object> map) {
        AuthUser currentUser = UserContext.getCurrentUser();
        String enId = currentUser.getId();
        return ResultUtil.data(productsUtils.create(map, enId));
    }

    /**
     * 超级管理员添加产品
     *
     * @param map
     * @return
     */
//    @PostMapping("admin")
//    public ResultMessage createAdmin(@RequestBody Map<String, Object> map) {
//        AuthUser currentUser = UserContext.getCurrentUser();
//        String enId = currentUser.getId();
//        return ResultUtil.data(productsUtils.create(map, enId));
//    }

    /**
     * 厂商查找产品
     *
     * @param map
     * @return
     */
    @PostMapping("/search")
    public JSONObject search(@RequestBody(required = false) Map<String, Object> map) {
        AuthUser currentUser = UserContext.getCurrentUser();
        String enId = currentUser.getId();
        return productsUtils.searchProduct(enId);
    }


    /**
     * 厂商删除产品
     *
     * @return
     */
    @PostMapping("/delete/{id}")
    public ResultMessage delete(@PathVariable String id) {
        AuthUser currentUser = UserContext.getCurrentUser();
        return entProService.remove(new QueryWrapper<EntPro>().eq("pro_id",id)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


}