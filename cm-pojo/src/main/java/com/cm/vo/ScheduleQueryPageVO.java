package com.cm.vo;

import lombok.AllArgsConstructor;
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
public class ScheduleQueryPageVO implements Serializable {


    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程类型（1:必修, 2:选修, 3:公选）
     */
    private Integer courseType;

    /**
     * 考核方式（1:考试, 2:考查）
     */
    private Integer assessmentType;

    /**
     * 课程名称（冗余）
     */
    private String courseName;

    /**
     * 教师姓名（冗余）
     */
    private String teacherName;

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

}
