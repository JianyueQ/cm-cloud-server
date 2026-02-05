package com.cm.common.rabbitMQ.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 消息转换配置
 * 所有需要使用 RabbitMQ 的服务都可以引用此配置
 * @author 31373
 */
@Slf4j
@Configuration
@EnableRabbit
@ConditionalOnClass(MessageConverter.class)
public class RabbitMQMessageConfig {


    private final MessageConverter messageConverter;

    public RabbitMQMessageConfig(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    /**
     * JSON 消息转换器
     * 使 RabbitMQ 能够自动序列化/反序列化 Java 对象
     */
    @Bean
    public MessageConverter messageConverter() {
        log.info("开始初始化 RabbitMQ 消息转换器......");
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 自定义 RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        // 使用 cm-common 的消息转换器
        template.setMessageConverter(messageConverter);

        // 设置发送确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送成功");
            } else {
                log.error("消息发送失败: {}", cause);
            }
        });

        // 设置返回回调
        template.setReturnsCallback(returned -> {
            log.warn("消息被退回: {}", returned.getMessage());
        });

        return template;
    }
}
