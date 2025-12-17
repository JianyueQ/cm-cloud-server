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
public class UpdateAttendanceStatusDTO implements Serializable {

    /**
     * 考勤id
     */
    private Long attendanceInitiateId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 签到状态
     */
    private Integer status;

}
