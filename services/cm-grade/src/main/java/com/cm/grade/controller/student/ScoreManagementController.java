package com.cm.grade.controller.student;

import com.cm.common.core.result.Result;
import com.cm.grade.service.ScoreManagementService;
import com.cm.vo.GradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 31373
 */
@Slf4j
@RestController("studentScoreManagementController")
@RequestMapping("/student/score")
public class ScoreManagementController {

    @Autowired
    private ScoreManagementService scoreManagementService;

    /**
     * 根据课程ID和学生ID获取成绩
     */
    @GetMapping("/getGrade/{courseId}")
    public Result<GradeVO> getGradeByCourseAndStudent(@PathVariable Long courseId) {
        log.info("根据课程ID获取成绩, courseId={}", courseId);
        return Result.success(scoreManagementService.getGradeByCourseAndStudentByCourseId(courseId));
    }

}
