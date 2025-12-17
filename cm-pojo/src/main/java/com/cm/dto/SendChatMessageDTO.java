package com.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendChatMessageDTO implements Serializable {

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 消息类型（1:文本, 2:图片, 3:文件, 4:语音）
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
     * 消息状态（0:撤回, 1:正常）
     */
    private Integer status;


}
