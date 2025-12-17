package com.cm.mapper;

import com.cm.entity.AttendanceRecord;
import com.cm.entity.Grade;
import com.cm.vo.StatisticsScoreStudentsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface DataManagementMapper {

    /**
     * 获取某门课程的考勤记录
     * @param courseId 课程id
     * @param currentId 当前用户id
     * @return 考勤记录
     */
    List<AttendanceRecord> getAttendanceRecordsByCourseId(Long courseId, Long currentId);

    /**
     * 获取某门课程的迟到记录
     * @param courseId 课程id
     * @param currentId 当前用户id
     * @return 迟到记录
     */
    List<AttendanceRecord> getLateRecordsByCourseId(Long courseId, Long currentId);

    /**
     * 获取某门课程的缺勤记录
     * @param courseId 课程id
     * @param currentId 当前用户id
     * @return 缺勤记录
     */
    List<AttendanceRecord> getAbsentRecordsByCourseId(Long courseId, Long currentId);

    /**
     * 获取某门课程的请假记录
     * @param courseId 课程id
     * @param currentId 当前用户id
     * @return 请假记录
     */
    List<AttendanceRecord> getLeaveRecordsByCourseId(Long courseId, Long currentId);


    /**
     * 管理员获取某门课程的考勤记录（通过状态筛选）
     * @param courseId 课程id
     * @param status 考勤状态
     * @return 考勤记录列表
     */
    List<AttendanceRecord> getRecordsByCourseIdAndStatus(Long courseId, Integer status);

    /**
     * 获取某门课程的评分记录
     *
     * @param courseId  课程id
     * @param currentId 当前用户id
     * @return 评分记录
     */
    List<Grade> getGradeRecordsByCourseId(Long courseId, Long currentId);

    /**
     * 获取某门课程的评分信息
     *
     * @param courseId  课程id
     * @param currentId 当前用户id
     * @return 评分信息
     */
    List<StatisticsScoreStudentsVO> getGradeInfoByCourseId(Long courseId, Long currentId);
}
