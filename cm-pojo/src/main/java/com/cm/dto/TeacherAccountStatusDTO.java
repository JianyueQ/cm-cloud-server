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
public class TeacherAccountStatusDTO implements Serializable {

    /**
     * 教师账号id
     */
    private Long id;

    /**
     * 状态
     */
    private Integer status;
}
