package com.cm.dto;

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
public class ResourceDTO implements Serializable {

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
     * 父级资源id
     */
    private Long parentId;

}
