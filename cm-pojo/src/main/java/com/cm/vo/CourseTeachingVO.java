package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTeachingVO implements Serializable {

    /**
     * 学期（如2023-2024-1）
     */
    private String semester;

    /**
     * 授课班级名称
     */
    private String className;

    /**
     * 最大学生人数
     */
    private Integer maxStudentCount;

    /**
     * 当前学生人数
     */
    private Integer currentStudentCount;

    /**
     * 上课时间地点信息
     */
    private String scheduleInfo;

}
