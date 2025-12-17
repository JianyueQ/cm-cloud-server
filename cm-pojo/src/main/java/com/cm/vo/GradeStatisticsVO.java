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
public class GradeStatisticsVO implements Serializable {

    /**
     * 平均分
     */
    private Integer averageScore;

    /**
     * 优秀人数
     */
    private Integer excellentCount;

    /**
     * 良好人数
     */
    private Integer goodCount;

    /**
     * 及格人数
     */
    private Integer passCount;

    /**
     * 不及格人数
     */
    private Integer failCount;

}
