package com.cm.grade.controller.admin;

import com.cm.common.core.result.Result;
import com.cm.dto.GradeAddDTO;
import com.cm.grade.service.ScoreManagementService;
import com.cm.vo.CourseStudentTreeVO;
import com.cm.vo.GradeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController("adminScoreManagementController")
@RequestMapping("/admin/score")
public class ScoreManagementController {

    @Autowired
    private ScoreManagementService scoreManagementService;

    /**
     * 获取课程学生的树形控件的数据模型
     */
    @GetMapping("/courseStudentTree")
    public Result<List<CourseStudentTreeVO>> courseStudentTree(){
        log.info("获取课程学生的树形控件的数据模型");
        return Result.success(scoreManagementService.adminCourseStudentTree());
    }

    /**
     * 根据课程ID和学生ID获取成绩
     */
    @GetMapping("/getGrade/{courseId}/{studentId}")
    public Result<GradeVO> getGradeByCourseAndStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        log.info("根据课程ID和学生ID获取成绩, courseId={}, studentId={}", courseId, studentId);
        return Result.success(scoreManagementService.getGradeByCourseAndStudent(courseId, studentId));
    }

    /**
     * 修改成绩
     */
    @PutMapping("/update")
    public Result<String> updateGrade(@RequestBody GradeAddDTO gradeAddDTO) {
        log.info("修改成绩, grade={}", gradeAddDTO);
        scoreManagementService.updateGrade(gradeAddDTO);
        return Result.success();
    }
}
