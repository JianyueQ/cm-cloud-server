package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentCoursePageVO implements Serializable {

    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程类型（1:必修, 2:选修, 3:公选）
     */
    private Integer courseType;

    /**
     * 考核方式（1:考试, 2:考查）
     */
    private Integer assessmentType;

    /**
     * 上课时间地点信息
     */
    private String scheduleInfo;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 学期（如2023-2024-1）
     */
    private String semester;
}
