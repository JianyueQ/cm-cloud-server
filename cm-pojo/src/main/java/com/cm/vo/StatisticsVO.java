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
public class StatisticsVO implements Serializable {

    /**
     * 已签到人数
     */
    private Integer signedCount;

    /**
     * 迟到人数
     */
    private Integer lateCount;

    /**
     * 缺勤人数
     */
    private Integer absentCount;

    /**
     * 请假人数
     */
    private Integer leaveCount;
}
