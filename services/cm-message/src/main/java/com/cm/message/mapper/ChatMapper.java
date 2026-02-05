package com.cm.message.mapper;


import com.cm.common.aop.annotations.AutoFile;
import com.cm.common.enumeration.OperationType;
import com.cm.dto.ChatSessionListDTO;
import com.cm.entity.ChatMessage;
import com.cm.entity.ChatSession;
import com.cm.entity.ChatSessionMember;
import com.cm.vo.ChatMessageVO;
import com.cm.vo.ChatSessionListVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface ChatMapper {

    /**
     * 创建聊天会话
     *
     * @param chatSession 会话信息
     */
    @AutoFile(OperationType.INSERT)
    void insertChatSession(ChatSession chatSession);

    /**
     * 获取会话记录(私聊)
     *
     * @param userId1 用户A
     * @param userId2 用户B
     * @return 会话ID
     */
    Long findPrivateChatSession(@Param("userId1") long userId1, @Param("userId2") long userId2);

    /**
     * 创建会话成员
     *
     * @param chatSessionMember 会话成员信息
     */
    void insertChatSessionMember(ChatSessionMember chatSessionMember);

    /**
     * 获取会话列表
     *
     * @param chatSessionListDTO 查询参数
     * @return 会话列表
     */
    Page<ChatSessionListVO> findChatSessionList(ChatSessionListDTO chatSessionListDTO);

    /**
     * 获取会话对象的昵称和头像
     *
     * @param id        会话ID
     * @param currentId 当前登录用户ID
     * @return 昵称和头像
     */
    HashMap<String, String> findOpponentAndAvatar(@Param("id") Long id, @Param("currentId") Long currentId);

    /**
     * 获取会话对象ID
     * @param sessionId 会话ID
     * @param currentId 当前登录用户ID
     * @return 会话对象ID
     */
    Long findOpponentId(@Param("sessionId") Long sessionId, @Param("currentId") Long currentId);

    /**
     * 获取会话对象消息列表
     * @param sessionId 会话ID
     * @return 消息列表
     */
    List<ChatMessageVO> findChatMessagesBySessionId(Long sessionId);

    /**
     * 保存聊天消息
     * @param message 消息
     */
    void saveChatMessage(ChatMessage message);
}
