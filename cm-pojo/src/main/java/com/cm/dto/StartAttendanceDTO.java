package com.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartAttendanceDTO implements Serializable {

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 考勤类型（1:普通考勤, 2:请假, 3:补签）
     */
    private Integer attendanceType;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 考勤期限
     */
    private Integer duration;

    /**
     * 考勤密码
     */
    private String signInPassword;

    /**
     * 签到方式
     */
    private Integer signInType;

}
