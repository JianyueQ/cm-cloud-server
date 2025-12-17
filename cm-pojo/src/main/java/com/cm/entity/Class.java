package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级实体类
 * 对应数据库表: class
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Class implements Serializable {
    /**
     * 班级ID
     */
    private Long id;
    
    /**
     * 专业ID
     */
    private Long majorId;
    
    /**
     * 班级名称
     */
    private String name;
    
    /**
     * 班级编码
     */
    private String code;
    
    /**
     * 年级
     */
    private String grade;
    
    /**
     * 班主任ID（教师ID）
     */
    private Long classTeacherId;
    
    /**
     * 学生人数
     */
    private Integer studentCount;
    
    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID
     */
    private Long createUser;
    
    /**
     * 更新人ID
     */
    private Long updateUser;
    
    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDeleted;
}