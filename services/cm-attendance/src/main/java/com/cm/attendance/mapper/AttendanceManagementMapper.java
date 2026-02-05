package com.cm.attendance.mapper;



import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.AttendanceListPageDTO;
import com.cm.dto.AttendanceSignInDTO;
import com.cm.dto.AttendanceStudentListDTO;
import com.cm.entity.*;

import com.cm.vo.AttendanceListPageVO;
import com.cm.vo.AttendanceStudentListVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface AttendanceManagementMapper {

    /**
     * 获取课程信息
     *
     * @param courseId 课程id
     * @return 课程信息
     */
    CourseTeaching getCourseById(Long courseId);

    /**
     * 获取用户信息
     *
     * @param currentId 当前用户id
     * @return 用户信息
     */
    User getUserById(Long currentId);

    /**
     * 添加考勤信息
     * @param attendanceInitiate 考勤信息
     */
    @AutoFile(OperationType.INSERT)
    void insertAttendanceInitiate(AttendanceInitiate attendanceInitiate);

    /**
     * 修改考勤信息
     * @param attendanceInitiate 考勤信息
     */
    void updateAttendanceInitiate(AttendanceInitiate attendanceInitiate);

    /**
     * 获取考勤信息
     * @param attendanceListPageDTO 考勤信息
     * @return 考勤信息
     */
    Page<AttendanceListPageVO> listInitiate(AttendanceListPageDTO attendanceListPageDTO);

    /**
     * 获取课程下的所有学生id
     *
     * @param courseId 课程id
     * @return 学生id
     */
    List<CourseSelection> listStudentIdsByCourseId(Long courseId);

    /**
     * 获取考勤信息
     *
     * @param id 考勤id
     * @return 考勤信息
     */
    AttendanceInitiate getAttendanceInitiateById(Long id);

    /**
     * 获取学生信息
     * @param studentIds 学生ids
     * @return 学生信息
     */
    List<User> listStudentInfoByStudentIds(List<Long> studentIds);

    /**
     * 批量插入考勤记录
     * @param recordsToInsert 考勤记录
     */
    void batchInsertAttendanceRecords(List<AttendanceRecord> recordsToInsert);

    /**
     * 获取已签到/未签到的学生信息
     * @param attendanceStudentListDTO 获取信息
     * @return 学生信息
     */
    List<AttendanceStudentListVO> getStudentList(AttendanceStudentListDTO attendanceStudentListDTO);

    /**
     * 获取学生进行中的签到列表-分页查询
     * @param attendanceListPageDTO 获取信息
     * @return 签到信息
     */
    Page<AttendanceListPageVO> studentListInitiate(AttendanceListPageDTO attendanceListPageDTO);

    /**
     * 修改考勤记录
     * @param attendanceRecord 考勤记录
     */
    @AutoFile(OperationType.UPDATE)
    void updateAttendanceRecordWithStatus(AttendanceRecord attendanceRecord);

    /**
     * 教师修改考勤记录
     * @param attendanceRecord 考勤记录
     */
    @AutoFile(OperationType.UPDATE)
    void teacherUpdateAttendanceRecordWithStatus(AttendanceRecord attendanceRecord);

    /**
     * 获取考勤记录
     * @param attendanceSignInDTO 获取信息
     * @param currentId 当前用户id
     * @return 考勤记录
     */
    AttendanceRecord getAttendanceRecord(@Param("attendanceSignInDTO") AttendanceSignInDTO attendanceSignInDTO, Long currentId);
}
