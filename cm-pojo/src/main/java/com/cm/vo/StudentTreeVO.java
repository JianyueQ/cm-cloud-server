package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentTreeVO implements Serializable {

    /**
     * 学生id
      */
    private Long studentId;

    /**
     * 学生名称
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNo;


}
