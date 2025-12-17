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
public class Mem implements Serializable {

    /**
     * 总内存
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;
    /**
     * 剩余内存
     */
    private double free;
    /**
     * 使用率
     */
    private double usage;

}
