package com.cm.attendance.controller.student;

import com.cm.attendance.service.AttendanceManagementService;
import com.cm.attendance.strategy.Factory.SignInStrategyFactory;
import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.dto.AttendanceListPageDTO;
import com.cm.dto.AttendanceSignInDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 考勤管理-控制器-学生端
 * @author 31373
 */
@Slf4j
@RestController("studentAttendanceManagementController")
@RequestMapping("/student/attendance")
public class AttendanceManagementController {

    private final AttendanceManagementService attendanceManagementService;
    private final SignInStrategyFactory signInStrategyFactory;

    public AttendanceManagementController(AttendanceManagementService attendanceManagementService, SignInStrategyFactory signInStrategyFactory) {
        this.attendanceManagementService = attendanceManagementService;
        this.signInStrategyFactory = signInStrategyFactory;
    }

    /**
     * 获取正在进行的考勤列表-分页查询
     */
    @GetMapping("/initiate/list")
    public Result<PageResult> listInitiate(AttendanceListPageDTO attendanceListPageDTO) {
        log.info("获取正在进行的考勤列表-分页查询:{}", attendanceListPageDTO);
        return Result.success(attendanceManagementService.studentListInitiate(attendanceListPageDTO));
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
     * 校验位置签到/密码签到
     */
    @PostMapping("/signIn")
    public Result<String> signIn(@RequestBody AttendanceSignInDTO attendanceSignInDTO ) {
        log.info("校验位置签到/密码签到:{}", attendanceSignInDTO);
        signInStrategyFactory.getStrategy(attendanceSignInDTO.getSignInType()).executeSignIn(attendanceSignInDTO);
        return Result.success();
    }

}
