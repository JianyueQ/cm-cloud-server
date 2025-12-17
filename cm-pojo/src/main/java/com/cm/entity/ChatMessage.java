package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 发送者类型（1:学生, 2:教师, 3:管理员）
     */
    private Integer senderType;

    /**
     * 消息类型（1:文本, 2:图片, 3:文件, 4:语音）
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 文件URL（多个用逗号分隔）
     */
    private String fileUrls;

    /**
     * 文件名（多个用逗号分隔）
     */
    private String fileNames;

    /**
     * 引用消息ID
     */
    private Long quoteMessageId;

    /**
     * 消息状态（0:撤回, 1:正常）
     */
    private Integer status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    private Long createUser;

    /**
     * 更新者ID
     */
    private Long updateUser;

    /**
     * 删除标志（0:未删除, 1:已删除）
     */
    private Integer isDeleted;
}
