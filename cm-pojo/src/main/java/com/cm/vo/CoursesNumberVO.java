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
public class CoursesNumberVO implements Serializable {

    /**
     * 课程ID
     */
    private Long id;

    /**
     * 最大学生人数
     */
    private Integer maxStudentCount;

    /**
     * 当前学生人数
     */
    private Integer currentStudentCount;

}
