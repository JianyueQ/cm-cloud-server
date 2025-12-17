package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统计数据实体类
 * 对应数据库表: statistics_data
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsData implements Serializable {
    
    /**
     * 统计数据ID
     */
    private Long id;
    
    /**
     * 统计类型（如：user_count, course_count等）
     */
    private String statType;
    
    /**
     * 统计键（如：date:2023-01-01）
     */
    private String statKey;
    
    /**
     * 统计值
     */
    private BigDecimal statValue;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 统计时间
     */
    private LocalDateTime statTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDeleted;
}