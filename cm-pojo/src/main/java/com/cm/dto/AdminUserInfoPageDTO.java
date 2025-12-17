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
public class AdminUserInfoPageDTO extends Page implements Serializable {


    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 工号
     */
    private String adminNo;

    /**
     * 状态（0:禁用, 1:启用）
     */
    private Integer status;

}
