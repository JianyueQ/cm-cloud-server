package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告实体类
 * 对应数据库表: announcement
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement implements Serializable {
    
    /**
     * 公告ID
     */
    private Long id;
    
    /**
     * 公告标题
     */
    private String title;
    
    /**
     * 公告内容
     */
    private String content;
    
    /**
     * 公告类型（1:系统公告, 2:课程公告, 3:学校通知）
     */
    private Integer announcementType;
    
    /**
     * 优先级（1:一般, 2:重要, 3:紧急）
     */
    private Integer priority;
    
    /**
     * 发布人ID
     */
    private Long publisherId;
    
    /**
     * 发布人姓名（冗余）
     */
    private String publisherName;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 生效开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 生效结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 阅读次数
     */
    private Integer readCount;
    
    /**
     * 状态（0:草稿, 1:已发布, 2:已撤回）
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
     * 修改人姓名
     */
    private String updateUserName;
    
    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDeleted;
}