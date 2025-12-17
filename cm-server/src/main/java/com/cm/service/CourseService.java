package com.cm.service;

import com.cm.dto.*;
import com.cm.result.PageResult;
import com.cm.vo.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author 31373
 */
public interface CourseService {

    /**
     * 课程分页-管理端
     *
     * @param coursePageDTO 课程分页参数
     * @return 课程分页结果
     */
    PageResult pageQuery(CoursePageDTO coursePageDTO);

    /**
     * 添加课程
     *
     * @param courseDTO 添加的课程信息
     */
    void addCourse(CourseDTO courseDTO);

    /**
     * 课程教师分页
     * @param userWithTeacherInfoPageDTO 课程教师分页参数
     * @return 课程教师分页结果
     */
    PageResult listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO);

    /**
     * 课程分配教师
     * @param courseAssignTeacherDTO 课程分配教师参数
     */
    void assignTeacher(@Valid CourseAssignTeacherDTO courseAssignTeacherDTO);

    /**
     * 获取学院/专业树结构
     *
     * @return 学院/专业树结构
     */
    List<CollegeMajorTreeVO> collegeMajorTree();

    /**
     * 课程班级分页查询
     * @param classListPageDTO 课程班级分页参数
     * @return 课程班级分页结果
     */
    PageResult listClasses(ClassListPageDTO classListPageDTO);

    /**
     * 添加课程班级
     * @param assignClassToCourse 添加课程班级参数
     */
    void addClass(@Valid AssignClassToCourseDTO assignClassToCourse);

    /**
     * 课程详情
     * @param id 课程id
     * @return 课程详情
     */
    CourseDetailVO detail(String id);

    /**
     * 修改课程
     * @param courseDTO 修改的课程信息
     */
    void updateCourse(@Valid CourseDTO courseDTO);

    /**
     * 添加课程
     * @param courseChangeStatusDTO 课程状态信息
     */
    void changeStatus(@Valid CourseChangeStatusDTO courseChangeStatusDTO);

    /**
     * 删除课程
     * @param ids 课程id
     */
    void deleteCourse(List<Long> ids);

    /**
     * 课程分页-教师端
     * @param coursePageDTO 课程分页参数
     * @return 课程分页结果
     */
    PageResult pageQueryByTeacher(CoursePageDTO coursePageDTO);

    /**
     * 课程分页-学生端
     * @param coursePageDTO 课程分页参数
     * @return 课程分页结果
     */
    PageResult pageQueryByStudent(CoursePageDTO coursePageDTO);

    /**
     * 获取当前课程和最大学生数
     *
     * @param courseIds 课程分页参数
     * @return 课程和最大学生数
     */
    List<CoursesNumberVO> getCurrentAndMaxStudentCount(List<Long> courseIds);

    /**
     * 选课
     * @param studentSelectCourseDTO 课程id
     */
    void select(StudentSelectCourseDTO studentSelectCourseDTO);

    /**
     * 获取已选课程
     *
     * @return 已选课程分页结果
     */
    List<CourseSelectionVO> listSelectedCourse();

    /**
     * 取消选课
     *
     * @param cancelCourseSelectionDTO 课程id
     */
    void deleteCourseSelection(CancelCourseSelectionDTO cancelCourseSelectionDTO);

    /**
     * 获取课程树形列表
     * @return 课程树形列表
     */
    List<CourseListVO> listCourse();

    /**
     * 获取课程树形列表-学生端
     * @return 课程树形列表
     */
    List<CourseListVO> listCourseByStudent();

    /**
     * 获取课程树形列表-管理员端
     * @return 课程树形列表
     */
    List<CourseListVO> adminListCourse();

}
