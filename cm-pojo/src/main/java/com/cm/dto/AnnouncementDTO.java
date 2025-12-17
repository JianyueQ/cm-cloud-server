package com.cm.dto;

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
public class AnnouncementDTO implements Serializable {

    /**
     * 公告ID
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告类型（1:系统公告, 2:课程公告, 3:学校通知）
     */
    private Integer announcementType;

    /**
     * 优先级（1:一般, 2:重要, 3:紧急）
     */
    private Integer priority;

    /**
     * 生效开始时间
     */
    private LocalDateTime startTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态（0:草稿, 1:已发布, 2:已撤回）
     */
    private Integer status;


}
