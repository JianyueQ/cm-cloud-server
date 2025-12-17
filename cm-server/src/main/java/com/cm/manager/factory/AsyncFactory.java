package com.cm.manager.factory;


import com.cm.entity.AttendanceRecord;
import com.cm.entity.OperationLog;
import com.cm.service.AttendanceManagementService;
import com.cm.service.SysOperLogService;
import com.cm.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 * @author 31373
 */
@Slf4j
public class AsyncFactory {

    /**
     * 操作日志记录
     * @param operationLog 日志
     * @return 任务
     */
    public static TimerTask recordOper(OperationLog operationLog) {
        return new TimerTask() {
            @Override
            public void run() {
                //添加操作日志
                SpringUtils.getBean(SysOperLogService.class).insertOperLog(operationLog);
            }
        };
    }

    /**
     * 修改考勤状态
     *
     * @param attendanceId 考勤id
     * @return 任务
     */
    public static TimerTask updateAttendanceStatusTask(Long attendanceId) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    //修改考勤状态
                    SpringUtils.getBean(AttendanceManagementService.class).endAttendance(attendanceId);
                } catch (Exception e) {
                    log.error("修改考勤状态异常", e);
                }
            }
        };
    }

    /**
     * 添加考勤记录
     *
     * @param attendanceRecord 考勤记录信息
     * @return 任务
     */
    public static TimerTask addAttendanceRecordTask(AttendanceRecord attendanceRecord) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    //添加考勤记录
                    SpringUtils.getBean(AttendanceManagementService.class).addAttendanceRecord(attendanceRecord);
                } catch (Exception e) {
                    log.error("添加考勤记录异常", e);
                }
            }
        };
    }

}
