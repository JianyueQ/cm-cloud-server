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
public class AttendanceSignInDTO implements Serializable {

    /**
     * 考勤发起ID
     */
    private Long attendanceInitiateId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 签到类型
     */
    private Integer signInType;

    /**
     * 状态(1.进行中 2.已结束)
     */
    private Integer status;

    /**
     * 考勤状态
     */
    private Integer signInStatus;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 考勤密码
     */
    private String signInPassword;


}
