package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDetailVO implements Serializable {

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
     * 课程描述
     */
    private String description;

    /**
     * 理论学时
     */
    private Integer theoryHours;

    /**
     * 实践学时
     */
    private Integer practiceHours;

    /**
     * 总学时
     */
    private Integer totalHours;

    /**
     * 课程类型（1:必修, 2:选修, 3:公选）
     */
    private Integer courseType;

    /**
     * 考核方式（1:考试, 2:考查）
     */
    private Integer assessmentType;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

    /**
     * 课程开始选择时间
     */
    private LocalDateTime selectionStartTime;

    /**
     * 课程结束选择时间
     */
    private LocalDateTime selectionEndTime;

    private CourseTeachingVO courseTeachingVO;
}
