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
public class CourseSelectionVO implements Serializable {

    /**
     * 选课记录ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

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
