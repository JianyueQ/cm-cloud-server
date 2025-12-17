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
public class CollegeMajorTreeVO implements Serializable {

    /**
     * 学院ID
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;

    /**
     * 专业列表Tree
     */
    private List<MajorTreeVO> children;
}
