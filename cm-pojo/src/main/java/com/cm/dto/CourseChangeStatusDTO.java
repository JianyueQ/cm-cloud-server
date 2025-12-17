package com.cm.dto;

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
public class CourseChangeStatusDTO implements Serializable {

    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程类型
     */
    private Integer courseType;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

}
