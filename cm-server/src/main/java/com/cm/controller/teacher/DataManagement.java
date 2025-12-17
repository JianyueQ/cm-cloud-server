package com.cm.controller.teacher;

import com.cm.result.Result;
import com.cm.service.DataManagementService;
import com.cm.vo.GradeStatisticsVO;
import com.cm.vo.StatisticsScoreStudentsVO;
import com.cm.vo.StatisticsStudentsVO;
import com.cm.vo.StatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController("teacherDataManagementController")
@RequestMapping("/teacher/data")
public class DataManagement {

    @Autowired
    private DataManagementService dataManagementService;

    /**
     * 查询考勤人数的统计数据
     *
     * @param courseId 课程id
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public Result<StatisticsVO> statistics(Long courseId) {
        log.info("查询统计数据:{}", courseId);
        return Result.success(dataManagementService.statistics(courseId));
    }

    /**
     * 获取迟到学生列表（迟到次数超过3次
     */
    @GetMapping("/lateStudents")
    public Result<List<StatisticsStudentsVO>> lateStudents(Long courseId) {
        log.info("获取迟到学生列表:{}", courseId);
        return Result.success(dataManagementService.lateStudents(courseId));
    }

    /**
     * 获取未签到学生列表（未签到次数超过3次）
     */
    @GetMapping("/absentStudents")
    public Result<List<StatisticsStudentsVO>> absentStudents(Long courseId) {
        log.info("获取未签到学生列表:{}", courseId);
        return Result.success(dataManagementService.absentStudents(courseId));
    }

    /**
     * 获取请假学生列表（请假次数超过3次）
     */
    @GetMapping("/leaveStudents")
    public Result<List<StatisticsStudentsVO>> leaveStudents(Long courseId) {
        log.info("获取请假学生列表:{}", courseId);
        return Result.success(dataManagementService.leaveStudents(courseId));
    }

    /**
     * 获取成绩统计信息
     */
    @GetMapping("/gradeStatistics")
    public Result<GradeStatisticsVO> gradeStatistics(Long courseId) {
        log.info("获取成绩统计信息:{}", courseId);
        return Result.success(dataManagementService.gradeStatistics(courseId));
    }

    /**
     * 获取成绩排行榜（前10名）
     */
    @GetMapping("/topStudents")
    public Result<List<StatisticsScoreStudentsVO>> topStudents(Long courseId) {
        log.info("获取成绩排行榜:{}", courseId);
        return Result.success(dataManagementService.topStudents(courseId));
    }

    /**
     * 获取需要关注的学生（成绩较低）
     */
    @GetMapping("/atRiskStudents")
    public Result<List<StatisticsScoreStudentsVO>> attentionStudents(Long courseId) {
        log.info("获取需要关注的学生:{}", courseId);
        return Result.success(dataManagementService.attentionStudents(courseId));
    }
}
