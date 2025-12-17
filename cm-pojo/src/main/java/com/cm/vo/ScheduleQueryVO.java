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
public class ScheduleQueryVO implements Serializable {

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 课程名称（冗余）
     */
    private String courseName;

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
     * 学期
     */
    private String semester;

    /**
     * 选课时间
     */
    private LocalDateTime selectionTime;

    /**
     * 选课状态（1:已选, 2:已取消, 3:已结课）
     */
    private Integer status;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 上课时间地点信息
     */
    private String scheduleInfo;

}
