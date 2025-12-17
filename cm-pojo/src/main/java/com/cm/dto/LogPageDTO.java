package com.cm.dto;

import com.cm.entity.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogPageDTO extends Page implements Serializable {

    /**
     * 操作地址
     */
    private String ipAddress;

    /**
     * 系统模块
     */
    private String module;

    /**
     * 操作人员
     */
    private String operatorName;

    /**
     * 操作类型
     */
    private Integer operatorType;

    /**
     * 操作状态
     */
    private Integer status;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
