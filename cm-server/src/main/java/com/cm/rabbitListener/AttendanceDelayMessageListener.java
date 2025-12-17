package com.cm.rabbitListener;

import com.cm.service.AttendanceManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 31373
 */
@Component
@Slf4j
public class AttendanceDelayMessageListener {

    private final AttendanceManagementService attendanceManagementService;

    public AttendanceDelayMessageListener(AttendanceManagementService attendanceManagementService) {
        this.attendanceManagementService = attendanceManagementService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "attendance.delay.queue", durable = "true"),
            exchange = @Exchange(value = "attendance.delay.exchange", delayed = "true"),
            key = "attendance.delay.message.update"
    ))
    public void handleAttendanceDelayMessage(Long id) {
        // 处理延迟消息
        log.info("处理延迟消息: {}", id);
        attendanceManagementService.endAttendance(id);
    }


}
