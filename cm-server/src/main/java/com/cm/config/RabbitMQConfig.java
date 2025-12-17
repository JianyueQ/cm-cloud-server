package com.cm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 31373
 */
@Slf4j
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    /**
     * 配置消息转换器
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        log.info("配置消息转换器...Jackson2JsonMessageConverter");
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置 RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("配置RabbitTemplate...");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 显式设置消息转换器
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        // 开启消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // 消息发送成功
                log.info("消息发送成功");
            } else {
                // 消息发送失败
                log.error("消息发送失败: {}", cause);
            }
        });
        // 开启失败回调
        rabbitTemplate.setReturnsCallback(returned -> {
            // 消息路由失败处理
        });
        return rabbitTemplate;
    }
}
