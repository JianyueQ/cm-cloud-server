package com.cm.service;

import com.cm.vo.GradeStatisticsVO;
import com.cm.vo.StatisticsScoreStudentsVO;
import com.cm.vo.StatisticsStudentsVO;
import com.cm.vo.StatisticsVO;

import java.util.List;

/**
 * @author 31373
 */
public interface DataManagementService {
    /**
     * 统计人数
     *
     * @return 统计结果
     */
    StatisticsVO statistics(Long courseId);

    /**
     * 获取迟到学生
     *
     * @param courseId 课程id
     * @return 迟到学生
     */
    List<StatisticsStudentsVO> lateStudents(Long courseId);

    /**
     * 获取缺勤学生
     * @param courseId 课程id
     * @return 缺勤学生
     */
    List<StatisticsStudentsVO> absentStudents(Long courseId);

    /**
     * 获取请假学生
     * @param courseId 课程id
     * @return 请假学生
     */
    List<StatisticsStudentsVO> leaveStudents(Long courseId);

    StatisticsVO adminStatistics(Long courseId);

    List<StatisticsStudentsVO> adminLateStudents(Long courseId);

    List<StatisticsStudentsVO> adminAbsentStudents(Long courseId);

    List<StatisticsStudentsVO> adminLeaveStudents(Long courseId);

    GradeStatisticsVO gradeStatistics(Long courseId);

    List<StatisticsScoreStudentsVO> topStudents(Long courseId);

    List<StatisticsScoreStudentsVO> attentionStudents(Long courseId);

    GradeStatisticsVO adminGradeStatistics(Long courseId);

    List<StatisticsScoreStudentsVO> adminTopStudents(Long courseId);

    List<StatisticsScoreStudentsVO> adminAttentionStudents(Long courseId);



}
