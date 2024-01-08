package com.wteam.controller.alarmRecord;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wteam.framework.common.enums.ResultCode;
import com.wteam.framework.common.enums.ResultUtil;
import com.wteam.framework.common.mybatis.util.PageUtil;
import com.wteam.framework.common.vo.PageVO;
import com.wteam.framework.common.vo.ResultMessage;
import com.wteam.framework.common.vo.SearchVO;
import com.wteam.framework.modules.alarmRecord.entity.dos.AlarmRecord;
import com.wteam.framework.modules.alarmRecord.service.AlarmRecordService;
import com.wteam.framework.modules.entDevice.service.EntDevService;
import com.wteam.framework.modules.system.SmsUtil;
import com.wteam.framework.modules.system.VmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
@RestController
@RequestMapping("app/alarmrecord")
@Api(tags = "告警记录")
public class AlarmRecordController {

    @Autowired
    private AlarmRecordService alarmRecordService;

    @Autowired
    private EntDevService entDevService;

    @Resource
    private VmsUtil vmsUtil;

    @Resource
    private SmsUtil smsUtil;


    @GetMapping("reWrite/page")
    @ApiOperation("分页")
    public ResultMessage<IPage<AlarmRecord>> rePage(AlarmRecord entity,
                                                    SearchVO searchVo,
                                                    PageVO page) {

        IPage<AlarmRecord> data = alarmRecordService.rePage(PageUtil.<AlarmRecord>initPage(page), PageUtil.<AlarmRecord>initWrapper(entity, searchVo));
        return ResultUtil.data(data);
    }


    @GetMapping("page")
    @ApiOperation("分页")
    public ResultMessage<IPage<AlarmRecord>> page(AlarmRecord entity,
                                                                SearchVO searchVo,
                                                                PageVO page) {
        IPage<AlarmRecord> data = alarmRecordService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return ResultUtil.data(data);

    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public ResultMessage<AlarmRecord> get(@PathVariable("id") Long id) {
        AlarmRecord data = alarmRecordService.getById(id);

        return ResultUtil.data(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public ResultMessage save(@RequestBody AlarmRecord entity) {
        //效验数据
        return alarmRecordService.save(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);

    }

    @PutMapping
    @ApiOperation("修改")
    public ResultMessage update(@RequestBody AlarmRecord entity) {
        //效验数据
        return alarmRecordService.updateById(entity) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public ResultMessage delete(@RequestBody Long[] ids) {
        //效验数据
        return alarmRecordService.removeBatchByIds(Arrays.asList(ids)) ? ResultUtil.success() : ResultUtil.error(ResultCode.ERROR);
    }

//    @PostMapping("alertsReceived")
//    @ApiOperation("报警短信语音")
//    public ResultMessage alertsReceived(@RequestBody Map<String,Object>  map) {
//        //发送短信
//        smsUtil.sendAlarmSms(map);
//        //发送语音
//        vmsUtil.sendAlarmVms(map);
//        return ResultUtil.success();
//
//    }

}