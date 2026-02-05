package com.cm.message.service;

import com.cm.common.core.result.PageResult;
import com.cm.dto.ChatSessionDTO;
import com.cm.dto.ChatSessionListDTO;
import com.cm.dto.SendChatMessageDTO;
import com.cm.vo.ChatMessageVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * @author 31373
 */
public interface ChatService {
    /**
     * 创建会话
     * @param chatSessionDTO 会话信息
     */
    void createChat(ChatSessionDTO chatSessionDTO);

    /**
     * 获取会话列表
     * @param chatSessionListDTO 查询参数
     * @return 会话列表
     */
    PageResult getChatSessionList(ChatSessionListDTO chatSessionListDTO);

    /**
     * 获取聊天消息
     * @param sessionId 会话id
     * @return 聊天消息
     */
    List<ChatMessageVO> getChatMessageList(Long sessionId);

    /**
     * 发送消息
     * @param sendChatMessageDTO 发送消息信息
     */
    void sendMessage(@Valid SendChatMessageDTO sendChatMessageDTO);
}
