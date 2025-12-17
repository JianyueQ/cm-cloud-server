package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息推送记录实体类
 * 对应数据库表: message
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 消息标题
     */
    private String title;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型（1:系统消息, 2:课程消息, 3:个人消息）
     */
    private Integer messageType;
    
    /**
     * 发送人ID
     */
    private Long senderId;
    
    /**
     * 发送人姓名（冗余）
     */
    private String senderName;
    
    /**
     * 接收人ID
     */
    private Long receiverId;
    
    /**
     * 接收人姓名（冗余）
     */
    private String receiverName;
    
    /**
     * 是否已读（0:未读, 1:已读）
     */
    private Integer isRead;
    
    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
    
    /**
     * 优先级（1:一般, 2:重要）
     */
    private Integer priority;
    
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    
    /**
     * 状态（0:发送失败, 1:已发送, 2:已撤回）
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