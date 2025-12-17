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
public class OperationLogPageVO implements Serializable {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 操作状态（0:失败, 1:成功）
     */
    private Integer status;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
