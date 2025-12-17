package com.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelCourseSelectionDTO implements Serializable {

    private List<Long> ids;

    private List<Long> courseIds;

    private Long userId;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

}
