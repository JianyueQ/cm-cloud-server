package com.cm.service.serviceImpl;

import com.cm.context.BaseContext;
import com.cm.dto.ScheduleQueryDTO;
import com.cm.mapper.ScheduleQueryMapper;
import com.cm.result.PageResult;
import com.cm.service.ScheduleQueryService;
import com.cm.vo.ScheduleQueryPageVO;
import com.cm.vo.ScheduleQueryVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 31373
 */
@Service
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    @Autowired
    private ScheduleQueryMapper scheduleQueryMapper;

    /**
     * 查询课表
     *
     * @param scheduleQueryDTO 查询参数
     * @return 查询结果
     */
    @Override
    public PageResult listPage(ScheduleQueryDTO scheduleQueryDTO) {
        scheduleQueryDTO.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(scheduleQueryDTO.getPageNum(), scheduleQueryDTO.getPageSize());
        Page<ScheduleQueryPageVO> page = scheduleQueryMapper.listPage(scheduleQueryDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    @Override
    public ScheduleQueryVO detail(Long id) {
        Long currentId = BaseContext.getCurrentId();
        return scheduleQueryMapper.detail(id,currentId);
    }
}
