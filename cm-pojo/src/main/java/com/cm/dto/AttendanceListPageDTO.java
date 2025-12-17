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
public class AttendanceListPageDTO extends Page implements Serializable {

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 状态（1:进行中, 2:已结束）
     */
    private Integer status;

    /**
     * 用户id
     */
    private Long userId;

}
