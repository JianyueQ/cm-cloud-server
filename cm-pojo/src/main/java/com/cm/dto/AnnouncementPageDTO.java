package com.cm.dto;

import com.cm.entity.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementPageDTO extends Page implements Serializable {
    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型（1:系统公告, 2:课程公告, 3:学校通知）
     */
    private Integer announcementType;

    /**
     * 发布人姓名（冗余）
     */
    private String publisherName;

    /**
     * 状态（0:草稿, 1:已发布, 2:已撤回）
     */
    private Integer status;


}
