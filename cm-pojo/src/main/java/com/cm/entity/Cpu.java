package com.cm.entity;

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
public class Cpu implements Serializable {

    /**
     * 核心数量
     */
    private Integer coreNum;
    /**
     * 用户使用率
     */
    private Double used;
    /**
     * 系统使用率
     */
    private Double wait;
    /**
     * 当前空闲
     */
    private Double free;

}
