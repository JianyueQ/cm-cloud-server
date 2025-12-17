package com.cm.vo;

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
public class UserWithStudentInfoPageVO implements Serializable {

    /**
     *  id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 学历层次（1:专科, 2:本科, 3:研究生）
     */
    private Integer educationLevel;

    /**
     * 学籍状态（1:在读, 2:休学, 3:退学, 4:毕业）
     */
    private Integer status;

}
