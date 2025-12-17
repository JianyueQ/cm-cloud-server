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
public class Jvm implements Serializable {

    /**
     * jvm总内存
     */
    private double total;
    /**
     * jvm已用内存
     */
    private double used;

    /**
     * jvm剩余内存
     */
    private double free;
    /**
     * jvm使用率
     */
    private double usage;

}
