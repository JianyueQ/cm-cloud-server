package com.cm.dto;

import jakarta.validation.constraints.NotNull;
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
public class CourseAssignTeacherDTO implements Serializable {

    /**
     * 课程id
     */
    @NotNull(message = "课程不能为空,请重新选择课程")
    private Long courseId;

    /**
     * 教师id
     */
    @NotNull(message = "教师不能为空,请选择教师")
    private Long teacherId;

}
