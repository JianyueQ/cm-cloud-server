package com.cm.system.service.serviceImpl;


import com.cm.api.system.UserFeignClient;
import com.cm.common.core.exception.SystemException;
import com.cm.common.core.result.PageResult;
import com.cm.dto.LogPageDTO;
import com.cm.entity.OperationLog;
import com.cm.entity.User;
import com.cm.system.mapper.SysOperLogMapper;
import com.cm.system.service.SysOperLogService;
import com.cm.vo.OperationLogDetailVO;
import com.cm.vo.OperationLogPageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 31373
 */
@Slf4j
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    private final SysOperLogMapper sysOperLogMapper;
    private final UserFeignClient userFeignClient;

    public SysOperLogServiceImpl(SysOperLogMapper sysOperLogMapper, UserFeignClient userFeignClient) {
        this.sysOperLogMapper = sysOperLogMapper;
        this.userFeignClient = userFeignClient;
    }

    /**
     * 新增操作日志
     * @param operationLog 操作日志
     */
    @Override
    public void insertOperLog(OperationLog operationLog) {
        try {
            //从注册中心获取用户服务地址并获取用户信息
            User user = userFeignClient.getUserInfo(operationLog.getOperatorId());
            String realName = user.getRealName();
            Integer userType = user.getUserType();
            log.info("用户信息: {},{}", realName, userType);
            operationLog.setOperatorName(realName);
            operationLog.setOperatorType(userType);
            operationLog.setOperationTime(LocalDateTime.now());
            operationLog.setCreateTime(LocalDateTime.now());
            operationLog.setUpdateTime(LocalDateTime.now());
            sysOperLogMapper.insertOperlog(operationLog);
        } catch (Exception e) {
            throw new SystemException("请求用户信息失败,失败的原因: " + e.getMessage());
        }
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
