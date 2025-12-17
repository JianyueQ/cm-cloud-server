package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ChatSessionListVO implements Serializable {

    /**
     * 会话ID
     */
    private Long id;

    /**
     * 会话类型
     */
    private Integer sessionType;

    /**
     * 会话名称
     */
    private String sessionName;

    /**
     * 会话头像
     */
    private String avatar;

    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;

    /**
     * 最后一条消息内容
     */
    private String lastMessageContent;

    /**
     * 未读消息数
     */
    private Integer unreadCount;
}
