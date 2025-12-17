package com.cm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignClassToCourseDTO implements Serializable {

    /**
     * 班级ids(集合)
     */
    @NotNull(message = "班级不能为空,请选择班级")
    private List<Long> classIds;

    /**
     * 课程id
     */
    @NotNull(message = "课程不能为空,请选择课程")
    private Long courseId;

    /**
     * 课程名称
     */
    @NotNull(message = "课程名称不能为空")
    private String courseName;

    /**
     * 学生总数
     */
    @NotNull(message = "学生总数不能为空")
    private Integer totalStudentCount;

}
