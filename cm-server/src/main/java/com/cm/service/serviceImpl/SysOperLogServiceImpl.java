package com.cm.service.serviceImpl;

import com.cm.dto.LogPageDTO;
import com.cm.entity.OperationLog;
import com.cm.entity.User;
import com.cm.mapper.AdminUserInfoMapper;
import com.cm.mapper.SysOperLogMapper;
import com.cm.result.PageResult;
import com.cm.service.SysOperLogService;
import com.cm.vo.OperationLogDetailVO;
import com.cm.vo.OperationLogPageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 31373
 */
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Autowired
    private SysOperLogMapper sysOperLogMapper;
    @Autowired
    private AdminUserInfoMapper adminUserInfoMapper;

    /**
     * 新增操作日志
     * @param operationLog 操作日志
     */
    @Override
    public void insertOperLog(OperationLog operationLog) {
        //根据操作人id获取操作人名称和操作人类别
        User user = adminUserInfoMapper.selectById(operationLog.getOperatorId());
        operationLog.setOperatorName(user.getRealName());
        operationLog.setOperatorType(user.getUserType());
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setCreateTime(LocalDateTime.now());
        operationLog.setUpdateTime(LocalDateTime.now());
        sysOperLogMapper.insertOperlog(operationLog);
    }

    /**
     * 分页查询操作日志
     * @param logPageDTO 查询参数
     * @return 分页数据
     */
    @Override
    public PageResult logPage(LogPageDTO logPageDTO) {
        if (logPageDTO.getStartTime() !=  null || logPageDTO.getEndTime() != null){
            //开始时间为00:00:00
            logPageDTO.setStartTime(logPageDTO.getStartTime().withHour(0).withMinute(0).withSecond(0));
            //结束时间为23:59:59
            logPageDTO.setEndTime(logPageDTO.getEndTime().withHour(23).withMinute(59).withSecond(59));
        }
        PageHelper.startPage(logPageDTO.getPageNum(), logPageDTO.getPageSize());
        Page<OperationLogPageVO> page = sysOperLogMapper.logPage(logPageDTO);
        return PageResult.builder()
                .total(page.getTotal())
                .records(page.getResult())
                .build();
    }

    /**
     * 查询操作日志详情
     * @param id 日志id
     * @return 日志详情
     */
    @Override
    public OperationLogDetailVO logDetail(Long id) {
        OperationLogDetailVO operationLogDetailVO = new OperationLogDetailVO();
        OperationLog operationLog = sysOperLogMapper.selectById(id);
        BeanUtils.copyProperties(operationLog, operationLogDetailVO);
        return operationLogDetailVO;
    }

    /**
     * 删除操作日志
     * @param ids 日志id
     */
    @Override
    public void deleteLog(List<Long> ids) {
        sysOperLogMapper.deleteLogByIds(ids);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void emptyLog() {
        sysOperLogMapper.emptyLog();
    }
}
