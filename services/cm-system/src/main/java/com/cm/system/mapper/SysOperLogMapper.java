package com.cm.system.mapper;

import com.cm.dto.LogPageDTO;
import com.cm.entity.OperationLog;
import com.cm.vo.OperationLogPageVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface SysOperLogMapper {

    /**
     * 插入操作日志
     * @param operationLog 操作日志
     */
    void insertOperlog(OperationLog operationLog);

    /**
     * 分页查询操作日志
     * @param logPageDTO 查询参数
     * @return 分页数据
     */
    Page<OperationLogPageVO> logPage(LogPageDTO logPageDTO);

    /**
     * 查询操作日志详情
     * @param id 日志id
     * @return 日志详情
     */
    OperationLog selectById(Long id);

    /**
     * 删除操作日志-批量删除
     * @param ids 日志id
     */
    void deleteLogByIds(List<Long> ids);

    /**
     * 清空操作日志
     */
    void emptyLog();

}
