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
public class Sys implements Serializable {

    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 服务器IP
     */
    private String serverIp;
    /**
     * 操作系统
     */
    private String osName;
    /**
     * 系统架构
     */
    private String osArch;

}
