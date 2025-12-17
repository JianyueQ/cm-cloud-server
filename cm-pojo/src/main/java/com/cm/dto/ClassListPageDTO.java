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
public class ClassListPageDTO extends Page implements Serializable {

    /**
     * 班级名称
     */
    private String name;

    /**
     * 年级
     */
    private String grade;

    /**
     * 学院ID
     */
    private Long collegeId;

    /**
     * 专业ID
     */
    private Long majorId;

}
