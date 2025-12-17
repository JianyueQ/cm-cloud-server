package com.cm.dto;

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
public class AttendanceStudentListDTO implements Serializable {

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 考勤id
     */
    private Long attendanceInitiateId;

    /**
     * 考勤状态（1:已签到, 2:迟到, 3:未签到, 4:请假）
     */
    private List<Integer> status;

}
