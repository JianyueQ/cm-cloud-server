package com.cm.dto;

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
public class AdminUserInfoUpdateStatusDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

}
