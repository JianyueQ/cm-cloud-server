package com.cm.course.controller.teacher;

import com.cm.common.aop.annotations.Log;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.course.service.CourseService;
import com.cm.dto.*;
import com.cm.vo.CollegeMajorTreeVO;
import com.cm.vo.CourseDetailVO;
import com.cm.vo.CourseListVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@RestController("teacherCourseManagementController")
@RequestMapping("/teacher/course")
public class CourseManagementController {

    @Autowired
    private CourseService courseService;

    /**
     * 课程分页查询
     *
     * @param coursePageDTO 分页查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult> page(CoursePageDTO coursePageDTO) {
        log.info("课程分页查询: {}", coursePageDTO);
        coursePageDTO.setUserId(BaseContext.getCurrentId());
        return Result.success(courseService.pageQueryByTeacher(coursePageDTO));
    }

    /**
     * 添加课程
     */
    @Log(title = "教师端-课程管理-添加课程", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public Result<String> addCourse(@Valid @RequestBody CourseDTO courseDTO){
        log.info("添加课程:{}",courseDTO);
        courseService.addCourse(courseDTO);
        return Result.success();
    }

    /**
     * 获取学院/专业的树形列表
     */
    @GetMapping("/collegeMajorTree")
    public Result<List<CollegeMajorTreeVO>> collegeMajorTree(){
        log.info("获取学院/专业树形列表");
        return Result.success(courseService.collegeMajorTree());
    }

    /**
     * 获取班级列表-分页查询
     */
    @GetMapping("/classList")
    public Result<PageResult> listClasses(ClassListPageDTO classListPageDTO){
        log.info("获取班级列表-分页查询:{}", classListPageDTO);
        return Result.success(courseService.listClasses(classListPageDTO));
    }

    /**
     * 添加授课班级
     */
    @Log(title = "教师端-课程管理-添加授课班级", businessType = BusinessType.UPDATE)
    @PostMapping("/assignClass")
    public Result<String> addClass(@Valid @RequestBody AssignClassToCourseDTO assignClassToCourse){
        log.info("添加授课班级:{}", assignClassToCourse);
        courseService.addClass(assignClassToCourse);
        return Result.success();
    }

    /**
     * 获取课程详情
     */
    @GetMapping("/detail/{id}")
    public Result<CourseDetailVO> detail(@PathVariable String id){
        log.info("获取课程详情:{}", id);
        return Result.success(courseService.detail(id));
    }

    /**
     * 修改课程
     */
    @Log(title = "教师端-课程管理-修改课程", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public Result<String> updateCourse(@Valid @RequestBody CourseDTO courseDTO){
        log.info("修改课程:{}", courseDTO);
        courseService.updateCourse(courseDTO);
        return Result.success();
    }

    /**
     * 启用/禁用课程
     */
    @Log(title = "教师端-课程管理-启用/禁用课程", businessType = BusinessType.UPDATE)
    @PutMapping("/updateStatus")
    public Result<String> changeStatus(@Valid @RequestBody CourseChangeStatusDTO courseChangeStatusDTO){
        log.info("启用/禁用课程:{}", courseChangeStatusDTO);
        courseService.changeStatus(courseChangeStatusDTO);
        return Result.success();
    }

    /**
     * 删除课程
     */
    @Log(title = "教师端-课程管理-删除课程", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public Result<String> deleteCourse(@RequestParam List<Long> ids){
        log.info("删除课程:{}", ids);
        courseService.deleteCourse(ids);
        return Result.success();
    }

    /**
     * 获取课程树形列表
     */
    @GetMapping("/courseList")
    public Result<List<CourseListVO>> listCourse() {
        log.info("获取课程列表");
        return Result.success(courseService.listCourse());
    }
}
