package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSession implements Serializable {
    /**
     * 会话ID
     */
    private Long id;

    /**
     * 会话类型
     */
    private Integer sessionType;

    /**
     * 会话名称(群聊时使用)
     */
    private String sessionName;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 会话头像
     */
    private String avatar;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 最后一条消息ID
     */
    private Long lastMessageId;

    /**
     * 最后消息时间
     */
    private LocalDateTime lastMessageTime;

    /**
     * 会话状态(0:禁用, 1:启用)
     */
    private Integer status;

    /**
     * 发言状态(0:全体禁言,1:正常发言)
     */
    private Integer speakStatus;

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
