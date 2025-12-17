package com.cm.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.cm.WebSocketServer.ChatMessageWebSocketServer;
import com.cm.annotations.RedisCache;
import com.cm.constant.MapConstant;
import com.cm.constant.RedisConstant;
import com.cm.constant.TypeConstant;
import com.cm.context.BaseContext;
import com.cm.dto.ChatSessionDTO;
import com.cm.dto.ChatSessionListDTO;
import com.cm.dto.SendChatMessageDTO;
import com.cm.entity.ChatSession;
import com.cm.entity.ChatSessionMember;
import com.cm.mapper.ChatMapper;
import com.cm.result.PageResult;
import com.cm.service.ChatService;
import com.cm.vo.ChatMessageVO;
import com.cm.vo.ChatSessionListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public ChatServiceImpl(ChatMapper chatMapper, RedisTemplate<String, String> redisTemplate, RabbitTemplate rabbitTemplate) {
        this.chatMapper = chatMapper;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 创建聊天会话
     *
     * @param chatSessionDTO 会话信息
     */
    @Override
    @Transactional
    public void createChat(ChatSessionDTO chatSessionDTO) {
        Long currentId = BaseContext.getCurrentId();
        if (chatSessionDTO.getSessionType() == 1) {
            Long existingSessionId = chatMapper.findPrivateChatSession(
                    //确保两个用户ID以固定顺序进行比较
                    Math.min(currentId, chatSessionDTO.getUserId()),
                    Math.max(currentId, chatSessionDTO.getUserId())
            );

            if (existingSessionId != null) {
                // 会话已存在，直接返回
                return;
            }
        }
        ChatSession chatSession = new ChatSession();
        chatSession.setSessionType(chatSessionDTO.getSessionType());
        if (chatSessionDTO.getSessionName() != null) {
            chatSession.setSessionName(chatSessionDTO.getSessionName());
        }
        if (chatSessionDTO.getSessionType() == 2 && chatSessionDTO.getAvatar() != null) {
            chatSession.setAvatar(chatSessionDTO.getAvatar());
        }
        if (chatSessionDTO.getSpeakStatus() != null) {
            chatSession.setSpeakStatus(chatSessionDTO.getSpeakStatus());
        }
        chatSession.setCreatorId(currentId);
        chatSession.setLastMessageTime(LocalDateTime.now());
        if (chatSessionDTO.getSessionType() == 1) {
            chatSession.setMemberCount(2);
        }
        chatMapper.insertChatSession(chatSession);
        if (chatSessionDTO.getSessionType() == 2) {

        }
        //如果是私聊会话,则创建会话成员
        if (chatSessionDTO.getSessionType() == 1) {
            //chat_session_member
            List<ChatSessionMember> chatSessionMemberList = new ArrayList<>();
            ChatSessionMember chatSessionMemberByDTO = new ChatSessionMember();
            chatSessionMemberByDTO.setSessionId(chatSession.getId());
            chatSessionMemberByDTO.setUserId(chatSessionDTO.getUserId());
            chatSessionMemberByDTO.setUserType(chatSessionDTO.getUserType());
            chatSessionMemberByDTO.setNickname(chatSessionDTO.getNickname());
            chatSessionMemberByDTO.setCreateTime(LocalDateTime.now());
            chatSessionMemberByDTO.setUpdateTime(LocalDateTime.now());
            chatSessionMemberByDTO.setCreateUser(currentId);
            chatSessionMemberByDTO.setUpdateUser(currentId);
            chatSessionMemberList.add(chatSessionMemberByDTO);
            //通过Redis获取当前登录用户的用户类型和真实姓名
            Map<Object, Object> currentUserInfo = redisTemplate.opsForHash().entries(RedisConstant.JWT_ID_KEY + currentId);
            //添加当前登录用户对象到会话成员列表中
            ChatSessionMember chatSessionMemberWithCurrent = new ChatSessionMember();
            chatSessionMemberWithCurrent.setSessionId(chatSession.getId());
            chatSessionMemberWithCurrent.setUserId(currentId);
            Integer userType = Integer.valueOf(currentUserInfo.get(MapConstant.USER_TYPE).toString());
            chatSessionMemberWithCurrent.setUserType(userType);
            String realName = currentUserInfo.get(MapConstant.REAL_NAME).toString();
            chatSessionMemberWithCurrent.setNickname(realName);
            chatSessionMemberWithCurrent.setCreateTime(LocalDateTime.now());
            chatSessionMemberWithCurrent.setUpdateTime(LocalDateTime.now());
            chatSessionMemberWithCurrent.setCreateUser(currentId);
            chatSessionMemberWithCurrent.setUpdateUser(currentId);
            chatSessionMemberList.add(chatSessionMemberWithCurrent);
            for (ChatSessionMember chatSessionMember : chatSessionMemberList) {
                chatMapper.insertChatSessionMember(chatSessionMember);
            }
        }
    }

    /**
     * 获取会话列表
     *
     * @param chatSessionListDTO 查询参数
     * @return 会话列表
     */
    @RedisCache(keyPrefix = "chat:session:list:", keyParts = {
            "#chatSessionListDTO.pageNum", "#chatSessionListDTO.pageSize",
            "#chatSessionListDTO.sessionName", "#chatSessionListDTO.nickname",
            "#chatSessionListDTO.userId"
    }, expireTime = 1, timeUnit = TimeUnit.DAYS)
    @Override
    public PageResult getChatSessionList(ChatSessionListDTO chatSessionListDTO) {
        PageHelper.startPage(chatSessionListDTO.getPageNum(), chatSessionListDTO.getPageSize());
        Page<ChatSessionListVO> page = chatMapper.findChatSessionList(chatSessionListDTO);
        for (ChatSessionListVO session : page.getResult()) {
            // 私聊
            if (session.getSessionType() == 1) {
                // 查询对方的昵称作为会话名称和头像
                HashMap<String, String> opponent = chatMapper.findOpponentAndAvatar(
                        session.getId(),
                        BaseContext.getCurrentId()
                );
                if (opponent != null) {
                    session.setSessionName(opponent.get("nickname"));
                    session.setAvatar(opponent.get("avatar"));
                } else {
                    session.setSessionName("用户已注销");
                }
            }
        }
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    @Override
    public List<ChatMessageVO> getChatMessageList(Long sessionId) {
        //从redis中获取聊天消息
        String redisKey = RedisConstant.CHAT_MESSAGE_LIST + sessionId;

        List<String> cachedMessages = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            // Redis 中有缓存数据，解析并返回
            List<ChatMessageVO> result = new ArrayList<>();
            // 由于使用 leftPush，数据在 Redis 中是倒序的，需要反转以获得正确的时间顺序
            for (int i = cachedMessages.size() - 1; i >= 0; i--) {
                String messageJson = cachedMessages.get(i);
                try {
                    // 将 JSON 字符串转换为 ChatMessageVO 对象
                    ChatMessageVO messageVO = JSON.parseObject(messageJson, ChatMessageVO.class);
                    result.add(messageVO);
                } catch (Exception e) {
                    // 如果解析失败，跳过该消息
                    log.warn("解析 Redis 中的消息失败: {}", messageJson, e);
                }
            }
            return result;
        } else {
            //Redis 中没有数据，从数据库查询
            return chatMapper.findChatMessagesBySessionId(sessionId);
        }
    }

    /**
     * 发送消息
     *
     * @param sendChatMessageDTO 发送消息信息
     */
    @Override
    public void sendMessage(SendChatMessageDTO sendChatMessageDTO) {
        Long currentId = BaseContext.getCurrentId();
        Long sessionId = sendChatMessageDTO.getSessionId();
        //从缓存中获取获取对方用户id
        Object opponentId = redisTemplate.opsForHash().get(RedisConstant.CHAT_OPPONENT + currentId, sessionId.toString());
        if (opponentId == null) {
            //根据会话id获取聊天会话成员表中的对方用户id并存入redis
            opponentId = chatMapper.findOpponentId(sessionId, currentId);
            redisTemplate.opsForHash().put(RedisConstant.CHAT_OPPONENT + currentId, sessionId.toString(), opponentId.toString());
            //设置过期时间
            redisTemplate.expire(RedisConstant.CHAT_OPPONENT + currentId, 1, TimeUnit.HOURS);
        } else {
            //设置过期时间
            redisTemplate.expire(RedisConstant.CHAT_OPPONENT + currentId, 1, TimeUnit.HOURS);
        }
        //构造消息对象
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("messageType", sendChatMessageDTO.getMessageType());
        messageData.put("content", sendChatMessageDTO.getContent());
        messageData.put("quoteMessageId", sendChatMessageDTO.getQuoteMessageId());
        messageData.put("status", sendChatMessageDTO.getStatus());
        messageData.put("sessionId", sessionId);
        messageData.put("senderId", currentId);
        messageData.put("sendTime", LocalDateTime.now());
        Object userType = redisTemplate.opsForHash().get(RedisConstant.JWT_ID_KEY + currentId, MapConstant.USER_TYPE);
        messageData.put("senderType", userType);

        //将消息对象json化
        String message = JSON.toJSONString(messageData);
        ChatMessageWebSocketServer.sendMessageToUser(Long.valueOf(opponentId.toString()), message);
        //补充参数并存储到redis中
        messageData.put("createTime", LocalDateTime.now());
        messageData.put("updateTime", LocalDateTime.now());
        messageData.put("createUser", currentId);
        messageData.put("updateUser", currentId);
        String s = JSON.toJSONString(messageData);
        // 存储到 Redis List 中，按时间顺序
        String redisKey = RedisConstant.CHAT_MESSAGE_LIST + sessionId;
        redisTemplate.opsForList().leftPush(redisKey, s);
        // 限制列表长度，只保留最近100条消息
        redisTemplate.opsForList().trim(redisKey, 0, 99);
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
        //rabbitMq异步处理请求
        rabbitTemplate.convertAndSend("chat.message.exchange","chat.message.save", messageData);
    }


}
