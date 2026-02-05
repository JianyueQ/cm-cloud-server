package com.cm.course.mapper;

import com.cm.dto.ScheduleQueryDTO;
import com.cm.vo.ScheduleQueryPageVO;
import com.cm.vo.ScheduleQueryVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface ScheduleQueryMapper {
    /**
     * 查询课表
     * @param scheduleQueryDTO 查询参数
     * @return 查询结果
     */
    Page<ScheduleQueryPageVO> listPage(ScheduleQueryDTO scheduleQueryDTO);

    /**
     * 详情
     * @param id 详情的id
     * @param currentId 当前用户id
     * @return 详情数据
     */
    ScheduleQueryVO detail(Long id, Long currentId);
}
