package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class CollegeMajorClassVO implements Serializable {

    /**
     * 学院ID
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;

    /**
     * 学院编码
     */
    private String code;

    /**
     * 学院下的专业
     */
    private List<MajorVO> majorVO;


}
