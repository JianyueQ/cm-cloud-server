package com.cm.vo;

import lombok.AllArgsConstructor;
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
public class OperationLogDetailVO implements Serializable {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作人类别（1:学生, 2:教师, 3:管理员）
     */
    private Integer operatorType;

    /**
     * 请求方式（GET、POST等）
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应结果
     */
    private String responseResult;

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
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

}
