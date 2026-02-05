package com.cm.system.service.serviceImpl;

import com.cm.common.core.context.BaseContext;
import com.cm.entity.AttendanceRecord;
import com.cm.entity.Grade;
import com.cm.system.mapper.DataManagementMapper;
import com.cm.system.service.DataManagementService;
import com.cm.vo.GradeStatisticsVO;
import com.cm.vo.StatisticsScoreStudentsVO;
import com.cm.vo.StatisticsStudentsVO;
import com.cm.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 31373
 */
@Service
public class DataManagementServiceImpl implements DataManagementService {

    private final DataManagementMapper dataManagementMapper;

    public DataManagementServiceImpl(DataManagementMapper dataManagementMapper) {
        this.dataManagementMapper = dataManagementMapper;
    }

    /**
     * 统计人数
     *
     * @param courseId 课程ID
     * @return 统计结果
     */
    @Override
    public StatisticsVO statistics(Long courseId) {
        Long currentId = BaseContext.getCurrentId();
        // 根据课程ID获取考勤记录并进行统计
        List<AttendanceRecord> records = dataManagementMapper.getAttendanceRecordsByCourseId(courseId, currentId);
        return calculateStatistics(records);
    }

    /**
     * 获取迟到学生
     * @param courseId 课程id
     * @return 迟到学生列表
     */
    @Override
    public List<StatisticsStudentsVO> lateStudents(Long courseId) {
        Long currentId = BaseContext.getCurrentId();
        // 获取所有迟到记录
        List<AttendanceRecord> lateRecords = dataManagementMapper.getLateRecordsByCourseId(courseId, currentId);

        // 统计每个学生的迟到次数
        Map<Long, Long> lateCountMap = lateRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出迟到次数超过3次的学生
        // 获取学生姓名（从迟到记录中找到该学生的任意一条记录获取姓名）
        return lateCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名（从迟到记录中找到该学生的任意一条记录获取姓名）
                    String studentName = lateRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取缺勤学生
     * @param courseId 课程id
     * @return 缺勤学生列表
     */
    @Override
    public List<StatisticsStudentsVO> absentStudents(Long courseId) {
        Long currentId = BaseContext.getCurrentId();
        // 获取所有未签到记录
        List<AttendanceRecord> absentRecords = dataManagementMapper.getAbsentRecordsByCourseId(courseId, currentId);

        // 统计每个学生的未签到次数
        Map<Long, Long> absentCountMap = absentRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出未签到次数超过3次的学生
        return absentCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名（从未签到记录中找到该学生的任意一条记录获取姓名）
                    String studentName = absentRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取请假学生
     * @param courseId 课程id
     * @return 请假学生列表
     */
    @Override
    public List<StatisticsStudentsVO> leaveStudents(Long courseId) {
        Long currentId = BaseContext.getCurrentId();
        // 获取所有请假记录
        List<AttendanceRecord> leaveRecords = dataManagementMapper.getLeaveRecordsByCourseId(courseId, currentId);

        // 统计每个学生的请假次数
        Map<Long, Long> leaveCountMap = leaveRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出请假次数超过3次的学生
        return leaveCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名（从请假记录中找到该学生的任意一条记录获取姓名）
                    String studentName = leaveRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public StatisticsVO adminStatistics(Long courseId) {
        // 直接根据课程ID获取考勤记录并进行统计（不需要当前用户ID）
        List<AttendanceRecord> records = dataManagementMapper.getAttendanceRecordsByCourseId(courseId, null);
        return calculateStatistics(records);
    }

    @Override
    public List<StatisticsStudentsVO> adminLateStudents(Long courseId) {
        // 获取所有迟到记录（状态为2）
        List<AttendanceRecord> lateRecords = dataManagementMapper.getRecordsByCourseIdAndStatus(courseId, 2);

        // 统计每个学生的迟到次数
        Map<Long, Long> lateCountMap = lateRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出迟到次数超过3次的学生
        return lateCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名
                    String studentName = lateRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsStudentsVO> adminAbsentStudents(Long courseId) {
        // 获取所有未签到记录（状态为3）
        List<AttendanceRecord> absentRecords = dataManagementMapper.getRecordsByCourseIdAndStatus(courseId, 3);

        // 统计每个学生的未签到次数
        Map<Long, Long> absentCountMap = absentRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出未签到次数超过3次的学生
        return absentCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名
                    String studentName = absentRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsStudentsVO> adminLeaveStudents(Long courseId) {
        // 获取所有请假记录（状态为4）
        List<AttendanceRecord> leaveRecords = dataManagementMapper.getRecordsByCourseIdAndStatus(courseId, 4);

        // 统计每个学生的请假次数
        Map<Long, Long> leaveCountMap = leaveRecords.stream()
                .collect(Collectors.groupingBy(
                        AttendanceRecord::getStudentId,
                        Collectors.counting()
                ));

        // 找出请假次数超过3次的学生
        return leaveCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 3)
                .map(entry -> {
                    // 获取学生姓名
                    String studentName = leaveRecords.stream()
                            .filter(record -> record.getStudentId().equals(entry.getKey()))
                            .findFirst()
                            .map(AttendanceRecord::getStudentName)
                            .orElse("未知学生");

                    return StatisticsStudentsVO.builder()
                            .studentId(entry.getKey())
                            .studentName(studentName)
                            .count(entry.getValue().intValue())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public GradeStatisticsVO gradeStatistics(Long courseId) {

        Long currentId = BaseContext.getCurrentId();

        // 获取课程的所有成绩记录
        List<Grade> gradeRecords = dataManagementMapper.getGradeRecordsByCourseId(courseId,currentId);

        // 如果没有成绩记录，返回默认值
        if (gradeRecords.isEmpty()) {
            GradeStatisticsVO gradeStatisticsVO = new GradeStatisticsVO();
            gradeStatisticsVO.setAverageScore(0);
            gradeStatisticsVO.setExcellentCount(0);
            gradeStatisticsVO.setGoodCount(0);
            gradeStatisticsVO.setPassCount(0);
            gradeStatisticsVO.setFailCount(0);
            return gradeStatisticsVO;
        }

        // 计算平均分（只计算有效分数）
        int totalScore = 0;
        int validScoreCount = 0;
        // 优秀人数（90分及以上）
        int excellentCount = 0;
        // 良好人数（80-89分）
        int goodCount = 0;
        // 及格人数（60-79分）
        int passCount = 0;
        // 不及格人数（60分以下）
        int failCount = 0;

        for (Grade grade : gradeRecords) {
            if (grade.getScore() != null) {
                int score = grade.getScore().intValue();
                totalScore += score;
                validScoreCount++;
                if (score >= 90) {
                    excellentCount++;
                } else if (score >= 80) {
                    goodCount++;
                } else if (score >= 60) {
                    passCount++;
                } else {
                    failCount++;
                }
            }
        }

        // 构建返回对象
        GradeStatisticsVO gradeStatisticsVO = new GradeStatisticsVO();
        // 平均分
        gradeStatisticsVO.setAverageScore(validScoreCount > 0 ? totalScore / validScoreCount : 0);
        gradeStatisticsVO.setExcellentCount(excellentCount);
        gradeStatisticsVO.setGoodCount(goodCount);
        gradeStatisticsVO.setPassCount(passCount);
        gradeStatisticsVO.setFailCount(failCount);

        return gradeStatisticsVO;
    }

    /**
     * 获取课程的Top学生
     * @param courseId 课程ID
     * @return Top学生列表
     */
    @Override
    public List<StatisticsScoreStudentsVO> topStudents(Long courseId) {

        Long currentId = BaseContext.getCurrentId();
        // 获取课程的所有学生成绩信息
        List<StatisticsScoreStudentsVO> gradeInfoList = dataManagementMapper.getGradeInfoByCourseId(courseId,currentId);

        // 按成绩降序排序，取前10名
        return gradeInfoList.stream()
                .sorted((s1, s2) -> {
                    // 处理空成绩的情况
                    Integer score1 = s1.getScore();
                    Integer score2 = s2.getScore();

                    // 空成绩排在最后
                    if (score1 == null && score2 == null) {
                        return 0;
                    }
                    if (score1 == null) {
                        return 1;
                    }
                    if (score2 == null) {
                        return -1;
                    }

                    // 成绩降序排列
                    return score2.compareTo(score1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 获取课程的关注学生
     * @param courseId 课程ID
     * @return 关注学生列表
     */
    @Override
    public List<StatisticsScoreStudentsVO> attentionStudents(Long courseId) {
        Long currentId = BaseContext.getCurrentId();
        // 获取课程的所有学生成绩信息
        List<StatisticsScoreStudentsVO> gradeInfoList = dataManagementMapper.getGradeInfoByCourseId(courseId, currentId);

        // 按成绩升序排序，取前10名（成绩较低的学生）
        return gradeInfoList.stream()
                .sorted((s1, s2) -> {
                    // 处理空成绩的情况
                    Integer score1 = s1.getScore();
                    Integer score2 = s2.getScore();
                    // 空成绩排在最后
                    if (score1 == null && score2 == null) {
                        return 0;
                    }
                    if (score1 == null) {
                        return 1;
                    }
                    if (score2 == null) {
                        return -1;
                    }

                    // 成绩升序排列（低分在前）
                    return score1.compareTo(score2);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public GradeStatisticsVO adminGradeStatistics(Long courseId) {
        // 获取课程的所有成绩记录
        List<Grade> gradeRecords = dataManagementMapper.getGradeRecordsByCourseId(courseId,null);

        // 如果没有成绩记录，返回默认值
        if (gradeRecords.isEmpty()) {
            GradeStatisticsVO gradeStatisticsVO = new GradeStatisticsVO();
            gradeStatisticsVO.setAverageScore(0);
            gradeStatisticsVO.setExcellentCount(0);
            gradeStatisticsVO.setGoodCount(0);
            gradeStatisticsVO.setPassCount(0);
            gradeStatisticsVO.setFailCount(0);
            return gradeStatisticsVO;
        }

        // 计算平均分（只计算有效分数）
        int totalScore = 0;
        int validScoreCount = 0;
        // 优秀人数（90分及以上）
        int excellentCount = 0;
        // 良好人数（80-89分）
        int goodCount = 0;
        // 及格人数（60-79分）
        int passCount = 0;
        // 不及格人数（60分以下）
        int failCount = 0;

        for (Grade grade : gradeRecords) {
            if (grade.getScore() != null) {
                int score = grade.getScore().intValue();
                totalScore += score;
                validScoreCount++;
                if (score >= 90) {
                    excellentCount++;
                } else if (score >= 80) {
                    goodCount++;
                } else if (score >= 60) {
                    passCount++;
                } else {
                    failCount++;
                }
            }
        }

        // 构建返回对象
        GradeStatisticsVO gradeStatisticsVO = new GradeStatisticsVO();
        // 平均分
        gradeStatisticsVO.setAverageScore(validScoreCount > 0 ? totalScore / validScoreCount : 0);
        gradeStatisticsVO.setExcellentCount(excellentCount);
        gradeStatisticsVO.setGoodCount(goodCount);
        gradeStatisticsVO.setPassCount(passCount);
        gradeStatisticsVO.setFailCount(failCount);

        return gradeStatisticsVO;
    }

    @Override
    public List<StatisticsScoreStudentsVO> adminTopStudents(Long courseId) {
        // 获取课程的所有学生成绩信息
        List<StatisticsScoreStudentsVO> gradeInfoList = dataManagementMapper.getGradeInfoByCourseId(courseId,null);

        // 按成绩降序排序，取前10名
        return gradeInfoList.stream()
                .sorted((s1, s2) -> {
                    // 处理空成绩的情况
                    Integer score1 = s1.getScore();
                    Integer score2 = s2.getScore();

                    // 空成绩排在最后
                    if (score1 == null && score2 == null) {
                        return 0;
                    }
                    if (score1 == null) {
                        return 1;
                    }
                    if (score2 == null) {
                        return -1;
                    }

                    // 成绩降序排列
                    return score2.compareTo(score1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsScoreStudentsVO> adminAttentionStudents(Long courseId) {
        // 获取课程的所有学生成绩信息
        List<StatisticsScoreStudentsVO> gradeInfoList = dataManagementMapper.getGradeInfoByCourseId(courseId, null);

        // 按成绩升序排序，取前10名（成绩较低的学生）
        return gradeInfoList.stream()
                .sorted((s1, s2) -> {
                    // 处理空成绩的情况
                    Integer score1 = s1.getScore();
                    Integer score2 = s2.getScore();
                    // 空成绩排在最后
                    if (score1 == null && score2 == null) {
                        return 0;
                    }
                    if (score1 == null) {
                        return 1;
                    }
                    if (score2 == null) {
                        return -1;
                    }

                    // 成绩升序排列（低分在前）
                    return score1.compareTo(score2);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 根据考勤记录列表计算统计数据
     *
     * @param records 考勤记录列表
     * @return 统计结果
     */
    private StatisticsVO calculateStatistics(List<AttendanceRecord> records) {
        // 已签到人数
        int signedCount = 0;
        // 迟到人数
        int lateCount = 0;
        // 缺勤人数
        int absentCount = 0;
        // 请假人数
        int leaveCount = 0;

        for (AttendanceRecord record : records) {
            switch (record.getStatus()) {
                // 已签到
                case 1:
                    signedCount++;
                    break;
                // 迟到
                case 2:
                    lateCount++;
                    break;
                // 缺勤
                case 3:
                    absentCount++;
                    break;
                // 请假
                case 4:
                    leaveCount++;
                    break;
            }
        }

        return StatisticsVO.builder()
                .signedCount(signedCount)
                .lateCount(lateCount)
                .absentCount(absentCount)
                .leaveCount(leaveCount)
                .build();
    }
}
