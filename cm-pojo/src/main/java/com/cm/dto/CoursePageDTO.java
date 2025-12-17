package com.cm.dto;

import com.cm.entity.Page;
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
public class CoursePageDTO extends Page implements Serializable {

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
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

    /**
     * 学期（如2023-2024-1）
     */
    private String semester;

    /**
     * 当前时间
     */
    private LocalDateTime now;

    private Long userId;

}
