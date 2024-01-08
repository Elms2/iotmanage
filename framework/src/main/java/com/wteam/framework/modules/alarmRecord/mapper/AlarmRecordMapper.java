package com.wteam.framework.modules.alarmRecord.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wteam.framework.modules.alarmRecord.entity.dos.AlarmRecord;
import org.apache.ibatis.annotations.Mapper;



/**
 * 告警记录
 *
 * @author khai 951992121@qq.com
 * @since 1.0.0 2023-02-03
 */
@Mapper
public interface AlarmRecordMapper extends BaseMapper<AlarmRecord> {
	
}