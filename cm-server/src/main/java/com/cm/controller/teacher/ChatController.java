package com.cm.controller.teacher;

import com.cm.context.BaseContext;
import com.cm.dto.ChatSessionDTO;
import com.cm.dto.ChatSessionListDTO;
import com.cm.dto.SendChatMessageDTO;
import com.cm.result.PageResult;
import com.cm.result.Result;
import com.cm.service.ChatService;
import com.cm.vo.ChatMessageVO;
import com.cm.vo.ChatSessionListVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天会话控制器
 *
 * @author 31373
 */
@Slf4j
@RestController("teacherChatController")
@RequestMapping("/teacher/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 创建私聊会话或群聊会话
     */
    @PostMapping("/createChatSession")
    public Result<String> createChat(@RequestBody @Valid ChatSessionDTO chatSessionDTO) {
        log.info("创建会话: {}", chatSessionDTO);
        chatService.createChat(chatSessionDTO);
        return Result.success();
    }

    /**
     * 获取会话列表
     */
    @GetMapping("/listChatSession")
    public Result<PageResult> listChatSession(ChatSessionListDTO chatSessionListDTO) {
        log.info("获取会话列表:{}", chatSessionListDTO);
        chatSessionListDTO.setUserId(BaseContext.getCurrentId());
        PageResult chatSessionVO = chatService.getChatSessionList(chatSessionListDTO);
        return Result.success(chatSessionVO);
    }

    /**
     * 获取聊天消息
     */
    @GetMapping("/listChatMessage/{sessionId}")
    public Result<List<ChatMessageVO>> listChatMessage(@PathVariable Long sessionId) {
        log.info("获取聊天消息: {}", sessionId);
        List<ChatMessageVO> chatMessageVO = chatService.getChatMessageList(sessionId);
        return Result.success(chatMessageVO);
    }

    /**
     * 发送消息
     */
    @PostMapping("/sendMessage")
    public Result<String> sendMessage(@RequestBody @Valid SendChatMessageDTO sendChatMessageDTO) {
        log.info("发送消息: {}", sendChatMessageDTO);
        chatService.sendMessage(sendChatMessageDTO);
        return Result.success();
    }
}
