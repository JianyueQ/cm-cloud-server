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
public class AttendanceListPageVO implements Serializable {

    /**
     * 考勤发起ID
     */
    private Long id;

    /**
     * 课程名称（冗余）
     */
    private String courseName;

    /**
     * 教师姓名（冗余）
     */
    private String teacherName;

    /**
     * 考勤类型（1:普通考勤, 2:请假, 3:补签）
     */
    private Integer attendanceType;

    /**
     * 签到方式
     */
    private Integer signInType;

    /**
     * 考勤状态
     */
    private Integer status;

    /**
     * 签到状态
     */
    private Integer signInStatus;

}
