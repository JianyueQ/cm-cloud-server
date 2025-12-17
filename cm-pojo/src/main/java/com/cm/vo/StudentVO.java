package com.cm.vo;

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
public class StudentVO implements Serializable {

    /**
     * 学生ID
     */
    private Long id;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 入学年份
     */
    private Integer enrollmentYear;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学历层次（1:专科, 2:本科, 3:研究生）
     */
    private Integer educationLevel;

    /**
     * 学籍状态（1:在读, 2:休学, 3:退学, 4:毕业）
     */
    private Integer status;
}
