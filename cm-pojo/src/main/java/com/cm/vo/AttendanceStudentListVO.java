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
public class AttendanceStudentListVO implements Serializable {

    /**
     * 考勤记录id
     */
    private Long id;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 考勤状态
     */
    private Integer status;

}
