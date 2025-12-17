package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教学资源实体类
 * 对应数据库表: teaching_resource
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachingResource implements Serializable {
    
    /**
     * 资源ID
     */
    private Long id;
    
    /**
     * 资源标题
     */
    private String title;
    
    /**
     * 资源描述
     */
    private String description;
    
    /**
     * 资源类型（1:课件, 2:视频, 3:文档, 4:链接, 5:其他）
     */
    private Integer resourceType;

    /**
     * 父级资源id
     */
    private Long parentId;
    
    /**
     * 文件URL（多个用逗号分隔）
     */
    private String fileUrls;
    
    /**
     * 文件名（多个用逗号分隔）
     */
    private String fileNames;
    
    /**
     * 文件大小（多个用逗号分隔）
     */
    private String fileSizes;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 课程名称（冗余）
     */
    private String courseName;
    
    /**
     * 上传人ID
     */
    private Long uploaderId;
    
    /**
     * 上传人姓名（冗余）
     */
    private String uploaderName;
    
    /**
     * 下载次数
     */
    private Integer downloadCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID
     */
    private Long createUser;
    
    /**
     * 更新人ID
     */
    private Long updateUser;
    
    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    private Integer isDeleted;
}