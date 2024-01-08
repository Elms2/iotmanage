package com.wteam.contorller.file;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejlchina.searcher.BeanSearcher;
import com.ejlchina.searcher.SearchResult;
import com.ejlchina.searcher.util.MapUtils;
import com.wteam.framework.common.cache.Cache;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.exception.ServiceException;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.security.AuthUser;
import com.wteam.framework.common.security.context.UserContext;
import com.wteam.framework.common.security.enums.UserEnums;
import com.wteam.framework.common.utils.Base64DecodeMultipartFile;
import com.wteam.framework.common.utils.BeanUtil;
import com.wteam.framework.common.utils.CommonUtil;
import com.wteam.framework.common.utils.StringUtils;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.admin.entity.dos.Admin;
import com.wteam.framework.modules.admin.entity.dto.AdminDTO;
import com.wteam.framework.modules.admin.entity.vo.AdminVo;
import com.wteam.framework.modules.admin.service.AdminService;
import com.wteam.framework.modules.entInfo.entity.dos.EntInfo;
import com.wteam.framework.modules.entInfo.mapper.EntInfoMapper;
import com.wteam.framework.modules.entInfo.service.EntInfoService;
import com.wteam.framework.modules.file.entity.dos.File;
import com.wteam.framework.modules.file.entity.dto.FileDto;
import com.wteam.framework.modules.file.entity.vo.FileVo;
import com.wteam.framework.modules.file.mapper.FileMapper;
import com.wteam.framework.modules.file.plugin.FilePluginFactory;
import com.wteam.framework.modules.file.service.FileService;
import com.wteam.framework.modules.member.entity.dos.Member;
import com.wteam.framework.modules.member.service.MemberService;
import com.wteam.framework.modules.system.entity.dos.Setting;
import com.wteam.framework.modules.system.entity.enums.SettingEnum;
import com.wteam.framework.modules.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 文件上传接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@Slf4j
@RestController
@Api(tags = "文件")
@RequestMapping("/pc/file")
public class FileController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private FileService fileService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private FilePluginFactory filePluginFactory;
    @Autowired
    private Cache cache;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntInfoService entInfoService;
    @Autowired
    private EntInfoMapper entInfoMapper;


    /**
     * 注入 Bean 检索器，它检索出来的数据以 泛型 对象呈现
     */
    @Autowired
    private BeanSearcher beanSearcher;


    @PostMapping(value = "/super/{page}/{size}")
    public ResultMessage fileSearch(@PathVariable Long page, @PathVariable Integer size, @RequestBody FileDto fileDto) {


//        String enId = fileDto.getEnId();
//        String factId = fileDto.getParentId();
        Map<String, Object> build = new HashMap<>(10);

        if (ObjectUtil.isNull(fileDto.getEnId()) && ObjectUtil.isNotNull(fileDto.getParentId())) {
              build = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(FileVo::getParentId)
                    .sql("$1 = ? ", fileDto.getParentId())
                    .limit(page, size)
                    .build();
        } else if (ObjectUtil.isNotNull(fileDto.getEnId()) && ObjectUtil.isNotNull(fileDto.getParentId())) {
              build  = MapUtils.builder()
                    // 生成 SQL 条件
                    .field(FileVo::getEnId, FileVo::getParentId)
                    .sql(" $1 = ? and $2 = ? ", fileDto.getEnId(), fileDto.getParentId())
                    .limit(page, size)
                    .build();

        } else {
              build = MapUtils.builder().limit(page, size).build();
        }
        Map<String, Object> map = PageUtil.convertToMap(fileDto, build);
        SearchResult<FileVo> search = beanSearcher.search(FileVo.class, map);

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

    @PostMapping("uploadContent")
    public ResultMessage uploadContent(@RequestBody File name, MultipartFile file) {
        AuthUser authUser = UserContext.getCurrentUser();
        //如果用户未登录，则无法上传图片
        if (null == authUser) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        if (null == file) {
            throw new ServiceException(ResultCode.FILE_NOT_EXIST_ERROR);
        }
        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        if (null == setting || CharSequenceUtil.isBlank(setting.getSettingValue())) {
            throw new ServiceException(ResultCode.OSS_NOT_EXIST);
        }

        String result = null;
        String fileKey = CommonUtil.rename(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File();
        try {
            InputStream inputStream = file.getInputStream();
            //上传至第三方云服务或服务器
            result = filePluginFactory.filePlugin().inputStreamUpload(inputStream, fileKey);
            if (ObjectUtil.isNotNull(name)) {
                newFile.setName(name.getName());
            } else {
                newFile.setName(file.getOriginalFilename());
            }
            newFile.setFileSize(file.getSize());
            newFile.setFileType(file.getContentType());
            newFile.setFileKey(fileKey);
            newFile.setUrl(result);
            newFile.setCreateBy(authUser.getUsername());
            newFile.setOwnerId(authUser.getId());
            newFile.setOwnerName(authUser.getUsername());
            newFile.setUrl(result);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        }

        return ResultUtil.data(newFile);
    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody File entity) {
        //效验数据
        return fileService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }


    @GetMapping("/page")
    @ApiOperation("分页")
    public ResultMessage<IPage<File>> page(File entity,
                                           SearchVO searchVo,
                                           PageVO page) {
        QueryWrapper<File> wrapper = PageUtil.initWrapper(entity, searchVo);
        wrapper.orderByDesc("create_time");
        IPage<File> data = fileService.page(PageUtil.initPage(page), wrapper);
        return ResultUtil.data(data);
    }

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/file/{opened}")
    public ResultMessage<Object> upload(MultipartFile file,
                                        String base64,
                                        @PathVariable String opened,
                                        String description,
                                        String name) {


        AuthUser authUser = UserContext.getCurrentUser();
        String username = authUser.getUsername();
        //如果用户未登录，则无法上传图片
        if (authUser == null) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        if (file == null) {
            throw new ServiceException(ResultCode.FILE_NOT_EXIST_ERROR);
        }
        Setting setting = settingService.get(SettingEnum.OSS_SETTING.name());
        if (setting == null || CharSequenceUtil.isBlank(setting.getSettingValue())) {
            throw new ServiceException(ResultCode.OSS_NOT_EXIST);
        }


        if (CharSequenceUtil.isNotBlank(base64)) {
            //base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }
        String result;
        String fileKey = CommonUtil.rename(Objects.requireNonNull(file.getOriginalFilename()));
        File newFile = new File();
        try {
            InputStream inputStream = file.getInputStream();
            //上传至第三方云服务或服务器
            result = filePluginFactory.filePlugin().inputStreamUpload(inputStream, fileKey);
            //保存数据信息至数据库
//            newFile.setName(file.getOriginalFilename());
            if (ObjectUtil.isNotNull(name)) {
                newFile.setName(name);
            } else {
                newFile.setName(file.getOriginalFilename());
            }
            newFile.setFileSize(file.getSize());
            newFile.setFileType(file.getContentType());
            newFile.setFileKey(fileKey);
            newFile.setUrl(result);
            newFile.setCreateBy(authUser.getUsername());
            newFile.setOpened(opened);
            newFile.setOwnerId(authUser.getId());
            newFile.setOwnerName(username);

            if (ObjectUtil.isNotNull(description)) {
                newFile.setDescription(description);
            }


            //FIXME: 之前改过代码，不知道authUser的role是不是为空
            if (authUser.getRole().equals(UserEnums.MANAGER)) {
                newFile.setState("2");
                newFile.setEnId(authUser.getId());
            } else {
                Member member = memberService.query().eq("id", authUser.getId()).one();
                newFile.setEnId(member.getEnId());
            }
            fileService.save(newFile);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new ServiceException(ResultCode.OSS_EXCEPTION_ERROR);
        }
        return ResultUtil.data(result);
    }


    @ApiOperation(value = "企业管理员查看企业待审核的素材")
    @GetMapping("/queryWaitPass")
    public ResultMessage queryWaitPass(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize) {
        AuthUser currentUser = UserContext.getCurrentUser();
        QueryWrapper queryWrapper = new QueryWrapper<File>();
        queryWrapper.eq("state", "0");
        queryWrapper.eq("en_id", currentUser.getId());
        queryWrapper.orderByDesc("create_time");
        IPage<File> page = new Page<>(pageNumber, pageSize);
        IPage selectPage = fileMapper.selectPage(page, queryWrapper);
//        List<File> fileList = fileService.query().eq("en_id", currentUser.getId()).eq("state", "1").eq("status",0).list();
        if (null != selectPage) {
            return ResultUtil.data(selectPage);
        } else {
            return ResultUtil.error(ResultCode.FILE_EXAMINED);
        }

    }

    @ApiOperation(value = "企业管理员审核素材")
    @GetMapping("/examine/{id}/{state}")
    public ResultMessage examine(@PathVariable("id") String id, @PathVariable("state") String state) {
        if ("1" == state) {
            fileService.update().eq("id", id).set("state", 1).update();
            return ResultUtil.data("审核不通过");
        } else {
            fileService.update().eq("id", id).set("state", 2).update();
            return ResultUtil.data("审核通过");
        }
    }

    @ApiOperation(value = "企业管理员删除素材")
    @GetMapping("/deleteFile/{fileId}")
    public ResultMessage deleteFile(@PathVariable("fileId") String fileId) {
        boolean b = fileService.removeById(fileId);
        return b ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    //查询厂商自己企业下的所有素材
    @GetMapping("/bussinessFile")
    public ResultMessage bussinessFile() {//@RequestParam("pageNumber")int pageNumber,@RequestParam("pageSize")int pageSize
        /**
         * 查看当前厂商
         * 然后查到企业中parentid是这个厂商的企业
         * 然后查看所有企业的素材
         */
        //当前用户
        AuthUser currentUser = UserContext.getCurrentUser();
        //厂商自己和企业的admin列表-->ents
//        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        List<Admin> admins = adminService.query().eq("parent_id", currentUser.getId()).list();
//        admins.add(admin);
        //admins企业列表
        //entInfoList要返回的企业带有素材的列表
        List<EntInfo> entInfoList = new ArrayList<>();
        for (Admin admin1 : admins) {
            //弄一个entinfo对象,里面包装着素材
            EntInfo entInfo = entInfoService.query().eq("en_id", admin1.getId()).one();
            //查询这个企业下的素材
            ArrayList<File> fileList = new ArrayList<>();
            List<File> files = fileService.query().eq("en_id", admin1.getId()).list();

            if (!files.isEmpty()) {
                fileList.addAll(files);
                entInfo.setFileList(files);
            }
//            System.out.println("asfaffsafafadfdaf"+entInfo);
            entInfoList.add(entInfo);

        }
        if (!entInfoList.isEmpty()) {
            return ResultUtil.data(entInfoList);
        } else {
            throw new ServiceException(ResultCode.ERROR);
        }
    }

    //查询厂商自己企业下的所有素材--数量
    @GetMapping("/bussinessFileNumber")
    public ResultMessage bussinessFileNumber(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        /**
         * 查看当前厂商
         * 然后查到企业中parentid是这个厂商的企业
         * 然后查看所有企业的素材
         */
        Long size;
        AuthUser currentUser = UserContext.getCurrentUser();
//        Admin admin = adminService.query().eq("id", currentUser.getId()).one();
        size = (long) (cache.get("size"));
        if (null == size) {
            List<Admin> ents = adminService.query().eq("parent_id", currentUser.getId()).list();
            ArrayList<File> fileList = new ArrayList<>();
            fileList.addAll(fileService.query().eq("en_id", currentUser.getId()).list());
            for (Admin ent : ents) {
                List<File> files = fileService.query().eq("en_id", ent.getId()).list();
                fileList.addAll(files);
            }
            size = (long) fileList.size();
            cache.put("size", fileList.size(), 30L, TimeUnit.MINUTES);
            return ResultUtil.data(size);
        } else {
            return ResultUtil.data(size);
        }

    }


    /**
     * @param pageSize 每页显示的数量
     * @param pageNum  当前页码
     * @Description: subList 分页
     * <br> 1. 起始位置边界值处理： 当前页码 <= 0 情况处理； 当前页码 > 最大页码 情况处理
     * <br> 2. 终止位置边界值处理： 当前页码 <= 0 情况处理； 终止位置 <= 总记录数  情况处理
     * @version v1.0
     * @author wu
     * @date 2022/7/31 11:44
     */
    private List<?> subList(ArrayList<?> list, int pageSize, int pageNum) {
        int count = list.size(); // 总记录数
        // 计算总页数
        int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        // 起始位置
        int start = pageNum <= 0 ? 0 : (pageNum > pages ? (pages - 1) * pageSize : (pageNum - 1) * pageSize);
        // 终止位置
        int end = pageNum <= 0 ? (pageSize <= count ? pageSize : count) : (pageSize * pageNum <= count ? pageSize * pageNum : count);
        return list.subList(start, end);
    }
}
