package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业实体类
 * 对应数据库表: major
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Major implements Serializable {

    /**
     * 专业ID
     */
    private Long id;
    
    /**
     * 学院ID
     */
    private Long collegeId;
    
    /**
     * 专业名称
     */
    private String name;
    
    /**
     * 专业编码
     */
    private String code;
    
    /**
     * 专业描述
     */
    private String description;
    
    /**
     * 学制（年）
     */
    private Integer duration;
    
    /**
     * 学位类型
     */
    private String degreeType;
    
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