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
public class ClassVO implements Serializable {

    /**
     * 班级ID
     */
    private Long id;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 班级编码
     */
    private String code;

    /**
     * 学生人数
     */
    private Integer studentCount;

}
