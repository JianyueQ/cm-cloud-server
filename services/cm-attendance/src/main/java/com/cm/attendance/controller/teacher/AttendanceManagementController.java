package com.cm.attendance.controller.teacher;


import com.cm.attendance.service.AttendanceManagementService;
import com.cm.common.aop.annotations.Log;
import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.dto.AttendanceListPageDTO;
import com.cm.dto.AttendanceStudentListDTO;
import com.cm.dto.StartAttendanceDTO;
import com.cm.dto.UpdateAttendanceStatusDTO;
import com.cm.vo.AttendanceStudentListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 考勤管理-控制器-教师端
 * @author 31373
 */
@Slf4j
@RestController("teacherAttendanceManagementController")
@RequestMapping("/teacher/attendance")
public class AttendanceManagementController {

    private final AttendanceManagementService attendanceManagementService;

    public AttendanceManagementController(AttendanceManagementService attendanceManagementService) {
        this.attendanceManagementService = attendanceManagementService;
    }

    /**
     * 发起考勤
     */
    @Log(title = "教师端-发起考勤", businessType = BusinessType.INSERT)
    @PostMapping("/start")
    public Result<String> startAttendance(@RequestBody StartAttendanceDTO startAttendanceDTO) {
        log.info("发起考勤: {}", startAttendanceDTO);
        attendanceManagementService.startAttendance(startAttendanceDTO);
        return Result.success();
    }

    /**
     * 获取正在进行的考勤列表-分页查询
     */
    @GetMapping("/initiate/list")
    public Result<PageResult> listInitiate(AttendanceListPageDTO attendanceListPageDTO) {
        log.info("获取正在进行的考勤列表-分页查询:{}", attendanceListPageDTO);
        return Result.success(attendanceManagementService.listInitiate(attendanceListPageDTO));
    }

    /**
     * 结束考勤
     */
    @Log(title = "教师端-结束考勤", businessType = BusinessType.UPDATE)
    @PutMapping("/end/{id}")
    public Result<String> endAttendance(@PathVariable Long id) {
        log.info("结束考勤:{}", id);
        attendanceManagementService.endAttendance(id);
        return Result.success();
    }

    /**
     * 获取考勤的剩余时间
     */
    @GetMapping("/remainingTime/{id}")
    public Result<Map<Object, Object>> getRemainingTime(@PathVariable Long id) {
        log.info("获取考勤的剩余时间:{}", id);
        return Result.success(attendanceManagementService.getRemainingTime(id));
    }

    /**
     * 获取已签到/未签到的学生信息
     */
    @GetMapping("/studentList")
    public Result<List<AttendanceStudentListVO>> getStudentList(AttendanceStudentListDTO attendanceStudentListDTO) {
        log.info("获取已签到/未签到的学生信息:{}", attendanceStudentListDTO);
        return Result.success(attendanceManagementService.getStudentList(attendanceStudentListDTO));
    }

    /**
     * 修改学生考勤状态
     */
    @Log(title = "教师端-修改学生考勤状态", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStatus")
    public Result<String> updateStudentStatus(@RequestBody UpdateAttendanceStatusDTO updateAttendanceStatusDTO) {
        log.info("修改学生考勤状态:{}", updateAttendanceStatusDTO);
        attendanceManagementService.updateStudentStatus(updateAttendanceStatusDTO);
        return Result.success();
    }

}
