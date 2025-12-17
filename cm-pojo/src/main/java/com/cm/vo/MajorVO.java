package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorVO implements Serializable {

    /**
     * 专业ID
     */
    private Long id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业编码
     */
    private String code;

    /**
     * 专业下的班级信息
     */
    private List<ClassVO> classVO;

}
