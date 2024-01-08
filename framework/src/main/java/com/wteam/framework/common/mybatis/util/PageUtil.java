package com.wteam.framework.common.mybatis.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wteam.framework.common.utils.BeanUtil;
import com.wteam.framework.common.utils.StringUtils;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.SearchVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分页工具
 *
 * @author Chopper
 * @version v4.0
 * @since 2020/11/26 15:23
 */
public class PageUtil {


    /**
     * Mybatis-Plus分页封装
     *
     * @param page 分页VO
     * @param <T>  范型
     * @return 分页响应
     */
    //传入分页条件参数(为了把querywrapper的根据什么排序和正序倒数封装到Page对象里面,还有页码和页代销封装到这里面来)
    public static <T> Page<T> initPage(PageVO page) {

        Page<T> p;
        //页码
        int pageNumber = page.getPageNumber();
        //页大小
        int pageSize = page.getPageSize();
        //按照什么字段排序
        String sort = page.getSort();
        //正序倒序asc,desc
        String order = page.getOrder();

        if (pageNumber < 1) {
            //如果页码小于1,就设置为第一页
            pageNumber = 1;
        }
        if (pageSize < 1) {
            //如果页大小小于1,就设置页大小为10
            pageSize = 10;
        }
//        if (pageSize > 100) {
//            //如果页大小超过一百,就设置页大小为100
//            pageSize = 100;
//        }
        if (StrUtil.isNotBlank(sort)) {
            //如果有写是按照那个字段来进行排序,创建一个false布尔值
            Boolean isAsc = false;
            if (StrUtil.isBlank(order)) {
                //如果已经设置了是正序倒序,就把布尔值设置为false
                isAsc = false;
            } else {
                //如果没有设置根据那个属性正序倒序排序,那就-->
                //设置倒序就把isAsc就设置为false,正序就设置为true
                if ("desc".equals(order.toLowerCase())) {
                    isAsc = false;
                } else if ("asc".equals(order.toLowerCase())) {
                    isAsc = true;
                }
            }
            //用页码和页大小创建一个Page对象
            p = new Page<>(pageNumber, pageSize);
            //根据tf来添加正序倒序排列
            if (isAsc) {
                p.addOrder(OrderItem.asc(sort));
            } else {
                p.addOrder(OrderItem.desc(sort));
            }

        } else {
            //如果没有说按照哪个字段排序,就直接用页码和页大小去生成Page对象
            p = new Page<>(pageNumber, pageSize);
        }
        return p;
    }

    /**
     * 生成条件搜索 全对象对比 equals
     * 如果需要like 需要另行处理
     *
     * @param object 对象（根据对象构建查询条件）
     * @return 查询wrapper
     */
    public static <T> QueryWrapper<T> initWrapper(Object object) {
        return initWrapper(object, null);
    }

    /**
     * 生成条件搜索对象querywrapper
     *
     * @param object   对象
     * @param searchVo 查询条件
     * @return 查询wrapper
     */
    //获得一个querywrapper
    //传入 --entity实体类 SearchVO起始日期,终止日期
    public static <T> QueryWrapper<T> initWrapper(Object object, SearchVO searchVo) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //创建时间区间判定
        if (searchVo != null && StrUtil.isNotBlank(searchVo.getStartDate()) && StrUtil.isNotBlank(searchVo.getEndDate())) {
            //如果有传searchvo对象,而且对象金额初始或者终止时间不为空
            //将传入的字符串时间转化为date类型
            Date start = cn.hutool.core.date.DateUtil.parse(searchVo.getStartDate());
            Date end = cn.hutool.core.date.DateUtil.parse(searchVo.getEndDate());
            //设置查询条件加上时间在这个范围
            queryWrapper.between("create_time", start, DateUtil.endOfDay(end));
        }
        if (object != null) {
            /**
             * 如果有传实体类,获得这个对象的属性名数组
             * 遍历所有属性名
             * 获取属性名对应的值,如果有值而且不为空字符串
             * (驼峰转下划线是因为实体类是驼峰格式,然而数据库是下划线格式,所以这里就是要指数据库字段等于这个查询条件值)
             * 根据这个对象的属性值去做查询条件
             */
            String[] fieldNames = BeanUtil.getFiledName(object);
            //遍历所有属性
            for (int j = 0; j < fieldNames.length; j++) {
                //获取属性的名字
                String key = fieldNames[j];
                //获取值
                Object value = BeanUtil.getFieldValueByName(key, object);
                //如果值不为空才做查询处理
                if (value != null && !"".equals(value)) {
                    //字段数据库中，驼峰转下划线
                    queryWrapper.eq(StringUtils.camel2Underline(key), value);
                }
            }
        }
        return queryWrapper;
    }


    public static Map<String, Object> convertToMap(Object object, Map<String, Object> map) {
        if (object != null) {
            /**
             * 如果有传实体类,获得这个对象的属性名数组
             * 遍历所有属性名
             * 获取属性名对应的值,如果有值而且不为空字符串
             * (驼峰转下划线是因为实体类是驼峰格式,然而数据库是下划线格式,所以这里就是要指数据库字段等于这个查询条件值)
             * 根据这个对象的属性值去做查询条件
             */
            String[] fieldNames = BeanUtil.getFiledName(object);
            //遍历所有属性
            for (int j = 0; j < fieldNames.length; j++) {
                //获取属性的名字
                String key = fieldNames[j];
                //获取值
                Object value = BeanUtil.getFieldValueByName(key, object);
                //如果值不为空才做查询处理
                if (value != null && !"".equals(value)) {
                    //字段数据库中，驼峰转下划线
//                    queryWrapper.eq(StringUtils.camel2Underline(key), value);
                    if (!key.equals("enId") || !key.equals("parentId")) {
                        map.put(key, value);
                    }
                }
            }
        }

        return map;
    }

    /**
     * List 手动分页
     *
     * @param page 分页对象
     * @param list 分页集合
     * @return 范型结果
     */
    public static <T> List<T> listToPage(PageVO page, List<T> list) {

        int pageNumber = page.getPageNumber() - 1;
        int pageSize = page.getPageSize();

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        int fromIndex = pageNumber * pageSize;
        int toIndex = pageNumber * pageSize + pageSize;

        if (fromIndex > list.size()) {
            return new ArrayList<>();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * 转换分页类型
     *
     * @param originPage 原分页
     * @param records    新分页数据
     * @param <T>        新类型
     * @return 新类型分页
     */
    public static <T> IPage<T> convertPage(IPage originPage, List<T> records) {
        IPage<T> resultPage = new Page<>();
        if (originPage != null) {
            resultPage.setCurrent(originPage.getCurrent());
            resultPage.setPages(originPage.getPages());
            resultPage.setTotal(originPage.getTotal());
            resultPage.setSize(originPage.getSize());
            resultPage.setRecords(records);
        }
        return resultPage;
    }

}
