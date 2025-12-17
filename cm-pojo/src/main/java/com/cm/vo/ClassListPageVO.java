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
public class ClassListPageVO implements Serializable {

    /**
     * 班级ID
     */
    private Long id;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 班级编码
     */
    private String code;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学生人数
     */
    private Integer studentCount;

}
