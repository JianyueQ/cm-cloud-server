package com.cm.message.rabbitMQListener;

import com.cm.entity.ChatMessage;
import com.cm.message.mapper.ChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cm.common.core.utils.SecureAccessUtil.*;


/**
 * @author 31373
 */
@Slf4j
@Component
public class ChatMessageConsumer {

    private final ChatMapper chatMapper;

    public ChatMessageConsumer(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "chat.message.queue", durable = "true"),
            exchange = @Exchange(value = "chat.message.exchange", type = ExchangeTypes.DIRECT),
            key = "chat.message.save"
    ))
    public void handleMessageSave(Map<String, Object> messageData) {
        // 处理消息持久化
        log.info("保存消息: {}", messageData);

        ChatMessage message = new ChatMessage();
        // 安全地设置各个字段值
        message.setMessageType(getIntegerValue(messageData, "messageType", 0));
        message.setSenderId(getLongValue(messageData, "senderId", 0L));
        message.setSenderType(getIntegerValue(messageData, "senderType", 0));
        message.setSessionId(getLongValue(messageData, "sessionId", 0L));
        message.setContent(getStringValue(messageData, "content", ""));

        // 特殊处理 quoteMessageId，允许为 null
        Object quoteMsgIdObj = messageData.get("quoteMessageId");
        if (quoteMsgIdObj != null) {
            try {
                message.setQuoteMessageId(Long.valueOf(quoteMsgIdObj.toString()));
            } catch (NumberFormatException e) {
                log.warn("quoteMessageId 格式错误: {}", quoteMsgIdObj);
            }
        }

        message.setStatus(getIntegerValue(messageData, "status", 0));
        message.setSendTime(getLocalDateTimeValue(messageData, "sendTime"));
        message.setCreateTime(getLocalDateTimeValue(messageData, "createTime"));
        message.setUpdateTime(getLocalDateTimeValue(messageData, "updateTime"));
        message.setCreateUser(getLongValue(messageData, "createUser", 0L));
        message.setUpdateUser(getLongValue(messageData, "updateUser", 0L));

        // 保存消息
        chatMapper.saveChatMessage(message);
    }
}
