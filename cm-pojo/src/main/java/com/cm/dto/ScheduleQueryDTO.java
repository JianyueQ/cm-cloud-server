package com.cm.dto;

import com.cm.entity.Page;
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
public class ScheduleQueryDTO extends Page implements Serializable {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学期
     */
    private String semester;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 用户id
     */
    private Long userId;


}
