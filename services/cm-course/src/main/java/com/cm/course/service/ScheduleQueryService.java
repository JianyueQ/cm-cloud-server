package com.cm.course.service;

import com.cm.common.core.result.PageResult;
import com.cm.dto.ScheduleQueryDTO;
import com.cm.vo.ScheduleQueryVO;

/**
 * @author 31373
 */

public interface ScheduleQueryService {
    /**
     * 课程查询
     * @param scheduleQueryDTO 查询参数
     * @return 分页结果
     */
    PageResult listPage(ScheduleQueryDTO scheduleQueryDTO);

    /**
     * 课程详情
     * @param id 课程id
     * @return 课程详情
     */
    ScheduleQueryVO detail(Long id);
}
