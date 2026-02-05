package com.cm.system.controller.admin;


import com.cm.common.aop.annotations.Log;
import com.cm.common.core.result.PageResult;
import com.cm.common.core.result.Result;
import com.cm.common.enumeration.BusinessType;
import com.cm.dto.LogPageDTO;
import com.cm.entity.OperationLog;
import com.cm.system.service.SysOperLogService;
import com.cm.vo.OperationLogDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 * @author 31373
 */
@Slf4j
@RestController
@RequestMapping("/admin/operLog")
public class SysOperLogController {

    private final SysOperLogService sysOperLogService;

    public SysOperLogController(SysOperLogService sysOperLogService) {
        this.sysOperLogService = sysOperLogService;
    }

    /**
     * 获取操作日志-分页查询
     */
    @GetMapping("/logPage")
    public Result<PageResult> logPage(LogPageDTO logPageDTO){
        log.info("获取操作日志-分页查询:{}", logPageDTO);
        return Result.success(sysOperLogService.logPage(logPageDTO));
    }

    /**
     * 获取操作日志-详情
     */
    @GetMapping("/{id}")
    public Result<OperationLogDetailVO> logDetail(@PathVariable Long id){
        log.info("获取操作日志-详情:{}", id);
        return Result.success(sysOperLogService.logDetail(id));
    }

    /**
     * 删除操作日志
     */
    @Log(title = "操作日志-删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete")
    public Result<String> deleteLog(@RequestParam List<Long> ids){
        log.info("删除操作日志:{}", ids);
        sysOperLogService.deleteLog(ids);
        return Result.success();
    }

    /**
     * 清空操作日志
     */
    @Log(title = "操作日志-清空", businessType = BusinessType.DELETE)
    @DeleteMapping("/empty")
    public Result<String> emptyLog(){
        log.info("清空操作日志");
        sysOperLogService.emptyLog();
        return Result.success();
    }

    /**
     * 添加操作日志
     */
    @PostMapping
    public Result<String> addOperLog(@RequestBody OperationLog operationLog){
        log.info("添加操作日志:{}", operationLog);
        sysOperLogService.insertOperLog(operationLog);
        return Result.success();
    }
}
