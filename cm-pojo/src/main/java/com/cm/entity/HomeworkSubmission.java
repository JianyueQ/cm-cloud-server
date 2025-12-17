package com.cm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业提交记录实体类
 * 对应数据库表: homework_submission
 * @author 31373
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkSubmission implements Serializable {
    
    /**
     * 作业提交ID
     */
    private Long id;
    
    /**
     * 作业ID
     */
    private Long homeworkId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 学号（冗余）
     */
    private String studentNo;
    
    /**
     * 学生姓名（冗余）
     */
    private String studentName;
    
    /**
     * 授课ID
     */
    private Long courseTeachingId;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 课程名称（冗余）
     */
    private String courseName;
    
    /**
     * 教师ID
     */
    private Long teacherId;
    
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    
    /**
     * 提交文件URL（多个用逗号分隔）
     */
    private String fileUrls;
    
    /**
     * 提交文件名（多个用逗号分隔）
     */
    private String fileNames;
    
    /**
     * 提交文件大小（多个用逗号分隔）
     */
    private String fileSizes;
    
    /**
     * 得分
     */
    private BigDecimal score;
    
    /**
     * 教师评语
     */
    private String comment;
    
    /**
     * 状态（1:已提交, 2:已批改）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人ID（学生ID）
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