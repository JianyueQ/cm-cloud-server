package com.cm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 31373
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseListVO implements Serializable {

    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 选课状态（1:已选, 2:已取消, 3:已结课）
     */
    private Integer status;

}
