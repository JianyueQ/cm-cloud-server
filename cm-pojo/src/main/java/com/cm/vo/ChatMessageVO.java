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
public class ChatMessageVO implements Serializable {

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 引用消息ID
     */
    private Long quoteMessageId;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

}
