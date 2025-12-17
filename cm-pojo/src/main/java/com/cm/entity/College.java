package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学院实体类
 * 对应数据库表: college
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class College implements Serializable {
    /**
     * 学院ID
     */
    private Long id;
    
    /**
     * 学院名称
     */
    private String name;
    
    /**
     * 学院编码
     */
    private String code;
    
    /**
     * 院长姓名
     */
    private String dean;
    
    /**
     * 联系方式
     */
    private String contactInfo;
    
    /**
     * 学院描述
     */
    private String description;
    
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