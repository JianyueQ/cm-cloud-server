package com.cm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 管理员信息实体类
 * 对应数据库表: admin
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    /**
     * 管理员ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 工号
     */
    private String adminNo;
    
    /**
     * 职位
     */
    private String position;
    
    /**
     * 入职日期
     */
    private LocalDate hireDate;

    
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
    @JsonIgnore
    private Integer isDeleted;
}