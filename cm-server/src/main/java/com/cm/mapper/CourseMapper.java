package com.cm.mapper;

import com.cm.annotations.AutoFile;
import com.cm.dto.CancelCourseSelectionDTO;
import com.cm.dto.ClassListPageDTO;
import com.cm.dto.CoursePageDTO;
import com.cm.dto.UserWithTeacherInfoPageDTO;
import com.cm.entity.*;
import com.cm.enumeration.OperationType;
import com.cm.vo.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface CourseMapper {

    /**
     * 分页查询课程信息
     * @param coursePageDTO 查询条件
     * @return 课程信息
     */
    Page<CoursePageVO> pageQuery(CoursePageDTO coursePageDTO);

    /**
     * 添加课程信息
     * @param course 课程信息
     */
    @AutoFile(OperationType.INSERT)
    void addCourse(Course course);

    /**
     * 添加课程管理信息
     * @param courseTeaching 课程管理信息
     */
    @AutoFile(OperationType.INSERT)
    void addCourseTeaching(CourseTeaching courseTeaching);

    /**
     * 列出老师账户信息
     * @param userWithTeacherInfoPageDTO 查询条件
     * @return 老师账户信息
     */
    Page<UserWithTeacherInfoPageVO> listTeacherAccount(UserWithTeacherInfoPageDTO userWithTeacherInfoPageDTO);

    /**
     * 修改课程管理信息
     * @param courseTeaching 课程管理信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateCourseTeaching(CourseTeaching courseTeaching);

    /**
     * 列出学院专业树结构
     *
     * @return 学院专业树结构
     */
    List<CollegeMajorTreeVO> collegeMajorTree();

    /**
     * 列出班级信息
     * @param classListPageDTO 查询条件
     * @return 班级信息
     */
    Page<ClassListPageVO> listClasses(ClassListPageDTO classListPageDTO);

    /**
     * 根据课程id查询课程管理信息
     * @param courseId 课程id
     * @return 课程管理信息
     */
    CourseTeaching getCourseTeachingById(Long courseId);

    /**
     * 根据老师id查询老师信息
     * @param teacherId 老师id
     * @return 老师信息
     */
    Teacher getTeacherById(Long teacherId);

    /**
     * 根据班级ids查询学生id
     * @param classIds 班级id
     * @return 学生id
     */
    List<Long> getStudentIdsByClassIds(List<Long> classIds);

    /**
     * 保存选课信息
     * @param courseSelection 选课信息
     */
    @AutoFile(OperationType.INSERT)
    void saveCourseSelection(CourseSelection courseSelection);

    /**
     * 获取课程详情
     * @param id 课程id
     * @return 课程详情
     */
    CourseDetailVO detail(String id);

    /**
     * 修改课程信息
     * @param course 课程信息
     */
    @AutoFile(OperationType.UPDATE)
    void updateCourse(Course course);

    /**
     * 逻辑删除课程信息
     * @param ids 课程ID列表
     */
    void deleteCourse(List<Long> ids);

    /**
     * 逻辑删除课程教学信息
     * @param courseIds 课程ID列表
     */
    void deleteCourseTeaching(List<Long> courseIds);

    /**
     * 逻辑删除课程选课信息
     * @param courseIds 课程ID列表
     */
    void deleteCourseSelection(List<Long> courseIds);

    /**
     * 获取用户
     * @param currentId 当前用户ID
     * @return  用户
     */
    User getUserById(Long currentId);

    /**
     * 分页查询课程信息
     * @param coursePageDTO 查询条件
     * @return 课程信息
     */
    Page<CoursePageVO> pageQueryByTeacher(CoursePageDTO coursePageDTO);

    /**
     * 分页查询课程信息
     * @param coursePageDTO 查询条件
     * @return 课程信息
     */
    Page<StudentCoursePageVO> pageQueryByStudent(CoursePageDTO coursePageDTO);

    /**
     * 获取所有激活的课程选课数量
     * @return 课程选课数量
     */
    List<CoursesNumberVO> getAllActiveCourseSelectionCount(LocalDateTime now);

    /**
     * 获取学生id
     * @param currentId 当前用户ID
     * @return 学生id
     */
    Long getStudentIdByUserId(Long currentId);

    /**
     * 获取课程选课信息
     * @param studentId 学生id
     * @param courseId 课程id
     * @return 课程选课信息
     */
    CourseSelection getCourseSelectionByStudentAndCourse(Long studentId, Long courseId);

    /**
     * 获取课程选课信息
     * @param courseId 课程id
     * @return 课程选课信息
     */
    CourseSelection getFillCourseSelection(Long courseId);

    /**
     * 获取所有激活的课程选课数量
     * @return 课程选课数量
     */
    List<CoursesNumberVO> getAllActiveCourseSelectionCount();

    /**
     * 获取所有激活的课程
     * @param now 当前时间
     * @return 课程
     */
    List<Course> getAllActiveCourseBySelectionTime(LocalDateTime now);

    /**
     * 获取学生已选课程
     *
     * @param currentId 当前用户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 学生已选课程
     */
    List<CourseSelectionVO> listSelectedCourse(Long currentId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 取消课程
     * @param cancelCourseSelectionDTO 课程取消信息
     */
    void cancelCourseSelection(CancelCourseSelectionDTO cancelCourseSelectionDTO);

    /**
     * 获取课程树形列表
     * @param currentId 当前用户ID
     * @return 课程树形列表
     */
    List<CourseListVO> listCourse(Long currentId);

    /**
     * 删除课程选课信息
     * @param courseId 课程id
     */
    void deleteCourseSelectionByCourseId(Long courseId);

    /**
     * 获取学生课程列表
     * @param currentId 当前用户ID
     * @return 学生课程列表
     */
    List<CourseListVO> listCourseByStudent(Long currentId);

    /**
     * 获取管理员课程列表
     * @return 管理员课程列表
     */
    List<CourseListVO> listCourseByAdmin();


}
